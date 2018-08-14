package deus.seow.de.fowtf;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import deus.seow.de.fowtf.db.AppDatabase;
import deus.seow.de.fowtf.db.table.Duel;
import deus.seow.de.fowtf.db.table.Player;
import deus.seow.de.fowtf.db.table.Tournament;

final class BackupXmlParser {

    private static final String TAG_DB = "db";
    private static final String TAG_PLAYER = "player";
    private static final String TAG_TOURNAMENT = "tournament";
    private static final String TAG_DUEL = "duel";
    private static final String TAG_ENTRY = "entry";


    private static final String ATTRIBUTE_ID = "id";
    private static final String ATTRIBUTE_TYPE = "type";
    private static final String ATTRIBUTE_DATE = "date";
    private static final String ATTRIBUTE_FIRSTNAME = "firstname";
    private static final String ATTRIBUTE_LASTNAME = "lastname";
    private static final String ATTRIBUTE_TOURNAMENT_ID = "tournamentId";
    private static final String ATTRIBUTE_ROUND = "round";
    private static final String ATTRIBUTE_PLAYER_ONE_ID = "playerOneId";
    private static final String ATTRIBUTE_PLAYER_TWO_ID = "playerTwoId";
    private static final String ATTRIBUTE_WINNER = "winner";


    private static final String NAMESPACE = null;

    private BackupXmlParser() {
        throw new AssertionError();
    }

    static void parse(File file, AppDatabase db) throws XmlPullParserException, IOException {
        XmlPullParser parser = Xml.newPullParser();
        try (InputStream fis = new BufferedInputStream(new FileInputStream(file))) {
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(fis, null);
            parser.nextTag();
            System.out.println("start parsing");
            switch (parser.getName()) {
                case TAG_DB:
                    readDb(parser, db);
                    System.out.println("finish parsing");
                    break;
                default:
                    throw new XmlPullParserException("unknown root tag discovered: " + parser.getName());
            }
        }

    }

    private static void readDb(XmlPullParser parser, AppDatabase db) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, NAMESPACE, TAG_DB);

        List<Tournament> tournaments = null;
        List<Player> players = null;
        List<Duel> duels = null;

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG)
                continue;

            switch (parser.getName()) {
                case TAG_TOURNAMENT:
                    tournaments = readTournaments(parser);
                    break;
                case TAG_PLAYER:
                    players = readPlayers(parser);
                    break;
                case TAG_DUEL:
                    duels = readDuels(parser);
                    break;
                default:
                    skip(parser);
                    break;
            }
        }
        saveToDb(tournaments, players, duels, db);
    }

    private static void saveToDb(List<Tournament> tournaments, List<Player> players, List<Duel> duels, AppDatabase db) {
        db.tournamentDao().insertAll(tournaments);
        db.playerDao().insertAll(players);
        db.duelDao().insertAll(duels);
    }

    private static List<Tournament> readTournaments(XmlPullParser parser) throws IOException, XmlPullParserException {

        List<Tournament> tournaments = new ArrayList<>();
        parser.require(XmlPullParser.START_TAG, NAMESPACE, TAG_TOURNAMENT);

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            switch (name) {
                case TAG_ENTRY:
                    tournaments.add(readTournament(parser));
                    break;
                default:
                    skip(parser);
                    break;
            }
        }

        return tournaments;
    }

    private static Tournament readTournament(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, NAMESPACE, TAG_ENTRY);

        int id = Integer.parseInt(parser.getAttributeValue(NAMESPACE, ATTRIBUTE_ID));
        String type = parser.getAttributeValue(NAMESPACE, ATTRIBUTE_TYPE);
        String date = parser.getAttributeValue(NAMESPACE, ATTRIBUTE_DATE);

        parser.nextTag();

        return new Tournament(id, type, date);
    }

    private static List<Player> readPlayers(XmlPullParser parser) throws IOException, XmlPullParserException {

        List<Player> players = new ArrayList<>();
        parser.require(XmlPullParser.START_TAG, NAMESPACE, TAG_PLAYER);

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            switch (name) {
                case TAG_ENTRY:
                    players.add(readPlayer(parser));
                    break;
                default:
                    skip(parser);
                    break;
            }
        }

        return players;
    }

    private static Player readPlayer(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, NAMESPACE, TAG_ENTRY);

        String id = parser.getAttributeValue(NAMESPACE, ATTRIBUTE_ID);
        String firstname = parser.getAttributeValue(NAMESPACE, ATTRIBUTE_FIRSTNAME);
        String lastname = parser.getAttributeValue(NAMESPACE, ATTRIBUTE_LASTNAME);

        parser.nextTag();

        return new Player(id, firstname, lastname);
    }

    private static List<Duel> readDuels(XmlPullParser parser) throws IOException, XmlPullParserException {

        List<Duel> duels = new ArrayList<>();
        parser.require(XmlPullParser.START_TAG, NAMESPACE, TAG_DUEL);

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            switch (name) {
                case TAG_ENTRY:
                    duels.add(readDuel(parser));
                    break;
                default:
                    skip(parser);
                    break;
            }
        }

        return duels;
    }

    private static Duel readDuel(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, NAMESPACE, TAG_ENTRY);

        int tournamentId = Integer.parseInt(parser.getAttributeValue(NAMESPACE, ATTRIBUTE_TOURNAMENT_ID));
        int round = Integer.parseInt(parser.getAttributeValue(NAMESPACE, ATTRIBUTE_ROUND));
        String playerOneId = parser.getAttributeValue(NAMESPACE, ATTRIBUTE_PLAYER_ONE_ID);
        String playerTwoId = parser.getAttributeValue(NAMESPACE, ATTRIBUTE_PLAYER_TWO_ID);
        String winner = parser.getAttributeValue(NAMESPACE, ATTRIBUTE_WINNER);

        return new Duel(tournamentId, round, playerOneId, playerTwoId, winner);
    }


    private static void skip(XmlPullParser parser) throws XmlPullParserException, IOException {

        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }
}
