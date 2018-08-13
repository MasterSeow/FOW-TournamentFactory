package deus.seow.de.fowtf;

import android.net.Uri;
import android.util.Xml;

import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import deus.seow.de.fowtf.db.AppDatabase;
import deus.seow.de.fowtf.db.table.Duel;
import deus.seow.de.fowtf.db.table.Player;
import deus.seow.de.fowtf.db.table.Tournament;

public class Backup {

    public static void createDbBackupFile(Uri uri, AppDatabase db){
        if(new File(uri.getPath()).isDirectory())
            System.out.println("isDirectory");
        File backupFile = new File(uri.getPath()+"/fow-tf-backup.xml");
        System.out.println(backupFile.getAbsolutePath());
        List<Tournament> tournaments = db.tournamentDao().getAll();
        List<Player> players = db.playerDao().getAllInc();
        List<Duel> duels = db.duelDao().getAll();
        if(backupFile.exists())
            backupFile.delete();
        try{
            backupFile.createNewFile();
        }catch(IOException e)
        {
           e.printStackTrace();
        }

        FileOutputStream fileos = null;
        try{
            fileos = new FileOutputStream(backupFile);

        }catch(FileNotFoundException e)
        {
            e.printStackTrace();
        }
        XmlSerializer serializer = Xml.newSerializer();
        try{
            serializer.setOutput(fileos, "UTF-8");
            serializer.startDocument(null, Boolean.valueOf(true));
            serializer.startTag(null,"db");
            serializer.startTag(null, "tournament");
            for (Tournament tournament : tournaments) {
                serializer.startTag(null, "entry");
                serializer.attribute(null,"id",String.valueOf(tournament.getId()));
                serializer.attribute(null,"type",tournament.getType());
                serializer.attribute(null,"date",tournament.getDate());
                serializer.endTag(null, "entry");
            }
            serializer.endTag(null,"tournament");
            serializer.startTag(null, "player");
            for (Player player : players) {
                serializer.startTag(null, "entry");
                serializer.attribute(null,"id",player.getId());
                serializer.attribute(null,"firstname",player.getFirstname());
                serializer.attribute(null,"lastname",player.getLastname());
                serializer.endTag(null, "entry");
            }
            serializer.endTag(null,"player");
            serializer.startTag(null, "duel");
            for (Duel duel : duels) {
                serializer.startTag(null, "entry");
                serializer.attribute(null,"tournamentId",String.valueOf(duel.getTournamentId()));
                serializer.attribute(null,"round",String.valueOf(duel.getRound()));
                serializer.attribute(null,"playerOneId",duel.getPlayerOneId());
                serializer.attribute(null,"playerTwoId",duel.getPlayerTwoId());
                serializer.attribute(null,"winner",duel.getWinner());
                serializer.endTag(null, "entry");
            }
            serializer.endTag(null,"duel");
            serializer.endTag(null,"db");
            serializer.endDocument();
            serializer.flush();
            fileos.close();

        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void loadDbBackupFile(Uri uri, AppDatabase db){
        File backupFile = new File(uri.getPath());
        if(backupFile.exists() && backupFile.getName().equals("fow-tf-backup.xml")){
            //TODO parse
        }

    }
}
