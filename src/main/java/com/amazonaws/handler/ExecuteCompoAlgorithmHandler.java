package com.amazonaws.handler;

import com.amazonaws.functions.compositionGenerator.ComplexCompositionGenerator;
import com.amazonaws.functions.compositionGenerator.CompositionGenerator;
import com.amazonaws.functions.compositionGenerator.GeneratorConfiguration;
import com.amazonaws.functions.compositionGenerator.ICompositionGenerator;
import com.amazonaws.model.CompositionType;
import com.amazonaws.model.Player;
import com.amazonaws.services.lambda.runtime.Context;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;

public class ExecuteCompoAlgorithmHandler extends AbstractHandler  {

    public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) throws IOException {
        super.handleRequest(inputStream, outputStream, context);
        JSONObject inputObject = this.asJSONObject(inputStream);
        GeneratorConfiguration config = this.getCompoGeneratorConfiguration(inputObject);
        List<Player> players = this.getPlayerListFromJsonObject(inputObject);
        ICompositionGenerator generator;
        if(config.getNbTeamsNeeded()==2) {
            generator = new CompositionGenerator(config);
        } else{
            generator = new ComplexCompositionGenerator(config);
        }
        new ObjectMapper().writeValue(outputStream, generator.getNBestCompositions(players));
    }

    private GeneratorConfiguration getCompoGeneratorConfiguration(JSONObject request){
        GeneratorConfiguration config = new GeneratorConfiguration();

        Object splitBestObject = request.get(RequestConstants.SPLIT_BEST_PLAYERS);
        if(splitBestObject instanceof Boolean){
            config.setSplitBestPlayers((Boolean)splitBestObject);
        }
        Object splitWorstObject = request.get(RequestConstants.SPLIT_WORST_PLAYERS);
        if(splitWorstObject instanceof  Boolean){
            config.setSplitWorstPlayers((Boolean)splitWorstObject);
        }
        Object splitGKObject = request.get(RequestConstants.SPLIT_GOAL_KEEPERS);
        if(splitGKObject instanceof Boolean){
            config.setSplitGoalKeepers((Boolean)splitGKObject);
        }
        Object splitDefendersObject = request.get(RequestConstants.SPLIT_DEFENDERS);
        if(splitDefendersObject instanceof Boolean){
            config.setSplitDefenders((Boolean)splitDefendersObject);
        }
        Object splitStrikersObject = request.get(RequestConstants.SPLIT_STRIKERS);
        if(splitStrikersObject instanceof Boolean){
            config.setSplitStrikers((Boolean)splitStrikersObject);
        }
        Object nbTeamsObject = request.get(RequestConstants.NB_TEAMS_NEEDED);
        if(nbTeamsObject instanceof String && ((String)nbTeamsObject).length()>0){
            config.setNbTeamsNeeded(Integer.valueOf((String)nbTeamsObject));
        }
        Object nbCompoObject = request.get(RequestConstants.NB_COMPOSITIONS_NEEDED);
        if(nbCompoObject instanceof String && ((String)nbCompoObject).length()>0){
            config.setNbCompositionsNeeded(Integer.valueOf((String)nbCompoObject));
        }
        Object gameTypeObject = request.get(RequestConstants.GAME_TYPE);
        if(gameTypeObject instanceof String && ((String)nbCompoObject).length()>0
                && Arrays.asList(CompositionType.values()).toString().contains((String)gameTypeObject)) {
            config.setCompositionType(CompositionType.valueOf((String)gameTypeObject));
        }
        this.getLogger().log("config = " + config);
        return config;
    }
}