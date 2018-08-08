package deus.seow.de.fowtf;

import java.util.List;

import deus.seow.de.fowtf.db.AppDatabase;
import deus.seow.de.fowtf.db.dao.DuelDao;
import deus.seow.de.fowtf.db.table.Player;
import deus.seow.de.fowtf.db.table.Tournament;

public class Util {

    public static String calculateTiebreak(Player player, Tournament tournament, DuelDao duelDao){
        String aa = getMatchpoints(duelDao.getWonRoundCount(player.getId(),tournament.getId()),duelDao.getDrawRoundCount(player.getId(),tournament.getId()));
        String bbb = getOpponentsWinrate();
        String ccc = getOpponentsOpponentsWinrate();
        String ddd = getSumOfLostRounds(duelDao.getLostRounds(player.getId(),tournament.getId()));
        //TODO
        return aa+bbb+ccc+ddd;
    }
    private static String getMatchpoints(int wonCount, int drawCount){
        int result = 3* wonCount + drawCount;
        return String.valueOf(result);
    }

    private static String getOpponentsWinrate(){
        return "";
    }

    private static String getOpponentsOpponentsWinrate(){
        return "";
    }

    // lostRound 2 and 3 -> 2^2+3^2
    private static String getSumOfLostRounds(List<Integer> lostRounds){
        int sum = 0;
        for (Integer lostRound : lostRounds) {
            sum += lostRound*lostRound;
        }
        return String.valueOf(sum);
    }

    /**
     * @return true if tb1 is bigger
     */
    public static boolean compareTiebreak(String tb1, String tb2){
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
