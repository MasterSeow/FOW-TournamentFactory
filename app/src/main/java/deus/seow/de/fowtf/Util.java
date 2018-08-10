package deus.seow.de.fowtf;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import deus.seow.de.fowtf.db.dao.DuelDao;
import deus.seow.de.fowtf.db.table.Player;

public class Util {

    public static List<Player> sortByTB(@NonNull List<Player> players, int tournamentId, @NonNull DuelDao duelDao) {
        List<Player> sortedPlayers = new ArrayList<>();

        HashMap<Player, String> tieBreakList = new HashMap<>();
        for (Player player : players) {
            tieBreakList.put(player, calculateTiebreak(player.getId(), tournamentId, duelDao));
        }

        for (int i = 0; i < tieBreakList.size(); i++)

            while (players.size() > 0) {
                Player highest = null;
                String highestTB = null;
                for (Player player : players) {
                    if (highest == null)
                        highest = player;
                    if (highestTB == null)
                        highestTB = tieBreakList.get(player);
                    if (compareTiebreak(tieBreakList.get(player), highestTB)) {
                        highest = player;
                        highestTB = tieBreakList.get(player);
                    }
                }
                sortedPlayers.add(highest);
                players.remove(highest);
            }

        return sortedPlayers;
    }

    public static String calculateTiebreak(String playerId, int tournamentId, @NonNull DuelDao duelDao) {
        String aa = getMatchpoints(duelDao.getWonRoundCount(playerId, tournamentId), duelDao.getDrawRoundCount(playerId, tournamentId));
        String bbb = getOpponentsWinrate(playerId, tournamentId, duelDao);
        String ccc = getOpponentsOpponentsWinrate(duelDao.getOpponent(tournamentId, duelDao.getRoundCount(tournamentId), playerId), tournamentId, duelDao);
        String ddd = getSumOfLostRounds(duelDao.getLostRounds(playerId, tournamentId));
        return aa + bbb + ccc + ddd;
    }

    private static String getMatchpoints(int wonCount, int drawCount) {
        int result = 3 * wonCount + drawCount;
        String resultString = String.valueOf(result);
        return resultString.length() == 1 ? "0" + resultString : resultString;
    }

    private static String getOpponentsWinrate(String playerId, int tournamentId, @NonNull DuelDao duelDao) {
        int i = 0;
        for (Player opponent : duelDao.getOpponents(tournamentId, playerId)) {
            i += (duelDao.getWonRoundCount(opponent.getId(), tournamentId) * 999) / duelDao.getRoundCount(tournamentId);
        }

        if (duelDao.getOpponents(tournamentId, playerId).size() > 0)
            i = i / duelDao.getOpponents(tournamentId, playerId).size();
        String resultString = String.valueOf(i);
        switch (resultString.length()) {
            case 1:
                resultString = "0" + resultString;
            case 2:
                resultString = "0" + resultString;
            default:
        }
        return resultString;
    }

    private static String getOpponentsOpponentsWinrate(Player player, int tournamentId, @NonNull DuelDao duelDao) {
        int i = 0;
        List<Player> opponentsOpponents = player == null ? new ArrayList<Player>() : duelDao.getOpponents(tournamentId, player.getId());
        for (Player opponentsOpponent : opponentsOpponents) {
            i += Integer.valueOf(getOpponentsWinrate(opponentsOpponent.getId(), tournamentId, duelDao));
        }
        if (opponentsOpponents.size() > 0)
            i = i / opponentsOpponents.size();

        String resultString = String.valueOf(i);
        switch (resultString.length()) {
            case 1:
                resultString = "0" + resultString;
            case 2:
                resultString = "0" + resultString;
            default:
        }
        return resultString;
    }

    // lostRound 2 and 3 -> 2^2+3^2
    private static String getSumOfLostRounds(List<Integer> lostRounds) {
        int sum = 0;
        for (Integer lostRound : lostRounds) {
            sum += lostRound * lostRound;
        }
        String resultString = String.valueOf(sum);
        switch (resultString.length()) {
            case 1:
                resultString = "0" + resultString;
            case 2:
                resultString = "0" + resultString;
            default:
        }
        return resultString;
    }

    /**
     * @return true if tb1 is bigger or equal
     */
    private static boolean compareTiebreak(String tb1, String tb2) {
        final char[] chars = tb1.toCharArray();
        final char[] chars1 = tb2.toCharArray();

        for (int i = 0; i < chars.length; i++) {
            int i1 = Integer.valueOf(String.valueOf(chars[i]));
            int i2 = Integer.valueOf(String.valueOf(chars1[i]));
            if (i1 < i2)
                return false;
            if (i1 > i2)
                return true;
        }

        return true;
    }
}
