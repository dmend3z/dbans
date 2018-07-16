package com.dnx.bans.mysql;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

import com.dnx.bans.Main;

import net.md_5.bungee.api.ProxyServer;

public class MySQLManager {

	public static MySQL db;
	  public static Statement statement;
	  
	  public static void setupDB()
	    throws SQLException
	  {
	    db = new MySQL(Main.getInstance().configs.hostname, Main.getInstance().configs.port, 
	      Main.getInstance().configs.database, Main.getInstance().configs.userName, 
	      Main.getInstance().configs.password);
	    try
	    {
	      db.openConnection();
	    }
	    catch (ClassNotFoundException e)
	    {
	      e.printStackTrace();
	    }
	    statement = db.getConnection().createStatement();
	    statement.executeUpdate(
	      "CREATE TABLE IF NOT EXISTS ban(user varchar(64), username text, byplayer varchar(64), reason text, time text, bannedin text);");
	    statement.executeUpdate(
	      "CREATE TABLE IF NOT EXISTS mute(user varchar(64), username text, byplayer varchar(64), reason text, time text, mutedin text);");
	    statement.executeUpdate("CREATE TABLE IF NOT EXISTS jogadores(uuid varchar(64), name text);");
	  }
	  
	  public static void criarJogador(UUID jogador)
	  {
	    if (playerJaLogou(jogador)) {
	      try
	      {
	        db.updateSQL("UPDATE jogadores SET name= '" + ProxyServer.getInstance().getPlayer(jogador).getName().toLowerCase() + "' WHERE uuid= '" + jogador.toString() + "';");
	      }
	      catch (ClassNotFoundException|SQLException e)
	      {
	        Main.debug("Um erro aconteceu ao atualizar o nick do jogador!");
	        e.printStackTrace();
	      }
	    } else {
	      try
	      {
	        db.updateSQL("INSERT INTO jogadores(uuid, name) VALUES ('" + jogador.toString() + "', '" + ProxyServer.getInstance().getPlayer(jogador).getName().toLowerCase() + "');");
	      }
	      catch (ClassNotFoundException|SQLException e)
	      {
	        Main.debug("Um erro aconteceu ao criar o perfil do jogador!");
	        e.printStackTrace();
	      }
	    }
	  }
	  
	  public static String getUUID(String nick)
	  {
	    String name = null;
	    try
	    {
	      ResultSet rs = db.querySQL("SELECT * FROM jogadores WHERE name='" + nick.toLowerCase() + "'");
	      if (rs.next()) {
	        name = rs.getString("uuid");
	      } else {
	        name = "ERROR";
	      }
	    }
	    catch (ClassNotFoundException|SQLException e)
	    {
	      e.printStackTrace();
	    }
	    return name;
	  }
	  
	  public static boolean playerJaLogou(UUID uuid)
	  {
	    try
	    {
	      ResultSet rs = db.querySQL("SELECT * FROM jogadores WHERE uuid='" + uuid.toString() + "'");
	      if (rs.next()) {
	        return rs.getString("uuid") != null;
	      }
	    }
	    catch (ClassNotFoundException|SQLException e)
	    {
	      e.printStackTrace();
	    }
	    return false;
	  }
	  
	  public static boolean playerJaLogou(String nick)
	  {
	    try
	    {
	      ResultSet rs = db.querySQL("SELECT * FROM jogadores WHERE name='" + nick.toLowerCase() + "'");
	      if (rs.next()) {
	        return rs.getString("uuid") != null;
	      }
	    }
	    catch (ClassNotFoundException|SQLException e)
	    {
	      e.printStackTrace();
	    }
	    return false;
	  }
	  
	  public static void closeDB()
	  {
	    try
	    {
	      db.closeConnection();
	    }
	    catch (SQLException e)
	    {
	      e.printStackTrace();
	    }
	  }
	}
