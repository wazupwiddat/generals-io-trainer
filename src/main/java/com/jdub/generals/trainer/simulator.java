package com.jdub.generals.trainer;


import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.zip.ZipFile;

/**
 * Created by jameswarren on 4/10/17.
 */
public class simulator {

    public static final String REPLAY_ZIP_FILE = "/trainingdata/generals.io replays.zip";

    public static void main(String args[]) {
        simulator sim = new simulator();
        sim.simulateReplays();
    }

    private void simulateReplays() {
        long startTime = System.currentTimeMillis();
        long endTime;
        try {
            try (ZipFile zipFile = new ZipFile(REPLAY_ZIP_FILE)) {
                zipFile.stream().forEach(zipEntry -> {
                    try (InputStream is = zipFile.getInputStream(zipEntry)) {
                        handleReplay(is);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            endTime = System.currentTimeMillis();
        }
        System.out.printf("Completed simulation in - %tT", endTime - startTime);
    }

    private void handleReplay(InputStream inputStream) {
        JSONParser parser = new JSONParser();
        try {
            JSONObject parsedReplay = (JSONObject)parser.parse(new InputStreamReader(inputStream));
            GameReplay replay = new GameReplay(parsedReplay);

            if (!replay.isInvalidReplay()) {
                Game game = new Game(replay);
                game.simulateGame();
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }
}
