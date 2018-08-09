package deus.seow.de.fowtf;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import deus.seow.de.fowtf.db.AppDatabase;
import deus.seow.de.fowtf.db.dao.DuelDao;
import deus.seow.de.fowtf.db.table.Player;
import deus.seow.de.fowtf.db.table.Tournament;

public class Util {

    public static List<Player> sortByTB(List<Player> players, int tournamentId, DuelDao duelDao){
        List<Player> sortedPlayers = new ArrayList<>();
        while (players.size()>0) {
            Player highest = null;
            String highestTB = null;
            for (Player player : players) {
                if(highest == null)
                    highest = player;
                if(highestTB == null)
                    highestTB = calculateTiebreak(player,tournamentId,duelDao);
                if(compareTiebreak(calculateTiebreak(player, tournamentId, duelDao), highestTB)){
                    highest = player;
                    highestTB =calculateTiebreak(player, tournamentId, duelDao);
                }
            }
            sortedPlayers.add(highest);
            players.remove(highest);
            System.out.println(highestTB);
        }

        return sortedPlayers;
    }

    private static String calculateTiebreak(Player player, int tournamentId, DuelDao duelDao){
        String aa = getMatchpoints(duelDao.getWonRoundCount(player.getId(),tournamentId),duelDao.getDrawRoundCount(player.getId(),tournamentId));
        String bbb = getOpponentsWinrate(player,tournamentId,duelDao);
        String ccc = getOpponentsOpponentsWinrate(duelDao.getOpponent(tournamentId,duelDao.getRoundCount(tournamentId),player.getId()),tournamentId,duelDao);
        String ddd = getSumOfLostRounds(duelDao.getLostRounds(player.getId(),tournamentId));
        //TODO
        return aa+bbb+ccc+ddd;
    }
    private static String getMatchpoints(int wonCount, int drawCount){
        int result = 3* wonCount + drawCount;
        String resultString = String.valueOf(result);
        return resultString.length() == 1? "0"+resultString:resultString;
    }

    private static String getOpponentsWinrate(Player player,int tournamentId, DuelDao duelDao ){
//        int i = 0;
//        for (Player opponent : duelDao.getOpponents(tournamentId, player.getId())) {
//            i += (duelDao.getWonRoundCount(opponent.getId(), tournamentId) / duelDao.getRoundCount(tournamentId))*500-1;
//        }
//
//        String resultString = String.valueOf(i);
//        switch (resultString.length()) {
//            case 1:
//                resultString = "0"+resultString;
//            case 2:
//                resultString = "0"+resultString;
//            default:
//        }
        return "000";//resultString;
    }

    private static String getOpponentsOpponentsWinrate(Player player,int tournamentId, DuelDao duelDao ){
//        int i = 0;
//        List<Player> opponentsOpponents = duelDao.getOpponents(tournamentId,player.getId());
//        for (Player opponentsOpponent : opponentsOpponents) {
//            i += Integer.valueOf(getOpponentsWinrate(opponentsOpponent,tournamentId,duelDao))/2;
//        }
//        i = i/opponentsOpponents.size();
//
//        String resultString = String.valueOf(i);
//        switch (resultString.length()) {
//            case 1:
//                resultString = "0"+resultString;
//            case 2:
//                resultString = "0"+resultString;
//            default:
//        }
        return "000";//resultString;
    }

    // lostRound 2 and 3 -> 2^2+3^2
    private static String getSumOfLostRounds(List<Integer> lostRounds){
        int sum = 0;
        for (Integer lostRound : lostRounds) {
            sum += lostRound*lostRound;
        }
        String resultString = String.valueOf(sum);
        switch (resultString.length()) {
            case 1:
                resultString = "0"+resultString;
            case 2:
                resultString = "0"+resultString;
            default:
        }
        return resultString;
    }

    /**
     * @return true if tb1 is bigger or equal
     */
    private static boolean compareTiebreak(String tb1, String tb2){
        final char[] chars = tb1.toCharArray();
        final char[] chars1 = tb2.toCharArray();

        for(int i = 0;i<chars.length;i++){
            int i1 = Integer.valueOf(String.valueOf(chars[i]));
            int i2 = Integer.valueOf(String.valueOf(chars1[i]));
            if(i1<i2)
                return false;
            if(i1>i2)
                return true;
        }

        return true;
    }
}
