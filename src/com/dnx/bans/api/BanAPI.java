package com.dnx.bans.api;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import com.dnx.bans.Main;
import com.dnx.bans.mysql.MySQLManager;

import net.md_5.bungee.api.ProxyServer;

public class BanAPI {

	public static void unban(String playername, UUID by)
	  {
	    try
	    {
	      if (by != null) {
	        ProxyServer.getInstance().getPlayer(by).sendMessage("§aProcessando pedido!");
	      } else {
	        ProxyServer.getInstance().getConsole().sendMessage("§aProcessando pedido!");
	      }
	      playername = playername.toLowerCase();
	      if (playerJaBanidoOffline(playername))
	      {
	        MySQLManager.db.updateSQL("DELETE FROM ban WHERE username='" + playername.toLowerCase() + "';");
	        if (by != null) {
	          ProxyServer.getInstance().getPlayer(by).sendMessage("§aPlayer foi desbanido!");
	        } else {
	          ProxyServer.getInstance().getConsole().sendMessage("§aPlayer foi desbanido!");
	        }
	      }
	      else if (by != null)
	      {
	        ProxyServer.getInstance().getPlayer(by).sendMessage("§cPlayer nao esta banido!");
	      }
	      else
	      {
	        ProxyServer.getInstance().getConsole().sendMessage("§cPlayer nao esta banido!");
	      }
	    }
	    catch (ClassNotFoundException|SQLException e)
	    {
	      ProxyServer.getInstance().getConsole().sendMessage("§cUm erro aconteceu ao desbanir o jogador");
	      e.printStackTrace();
	    }
	  }
	  
	  public static boolean banOnline(UUID player, String playername, UUID by, String byname, String reason, long time)
	  {
	    try
	    {
	      if (playerJaBanidoOnline(player))
	      {
	        if (by != null)
	        {
	          ProxyServer.getInstance().getPlayer(by).sendMessage("§cO jogador ja esta banido!");
	          return false;
	        }
	        ProxyServer.getInstance().getConsole().sendMessage("§cO jogador ja esta banido!");
	        return false;
	      }
	      if (by != null) {
	        ProxyServer.getInstance().getPlayer(by).sendMessage("§aProcessando pedido!");
	      } else {
	        ProxyServer.getInstance().getConsole().sendMessage("§aProcessando pedido");
	      }
	      PreparedStatement stm = MySQLManager.db.getConnection().prepareStatement(
	        "INSERT INTO `ban`(`user`, `username`, `byplayer`, `reason`, `time`, `bannedin`) VALUES (?, ?, ?, ?, ?, ?);");
	      stm.setString(1, player.toString());
	      stm.setString(2, playername.toLowerCase());
	      if (by == null) {
	        stm.setString(3, "CONSOLE");
	      } else {
	        stm.setString(3, by.toString());
	      }
	      stm.setString(4, reason);
	      stm.setLong(5, time);
	      stm.setLong(6, System.currentTimeMillis());
	      stm.executeUpdate();
	      ProxyServer.getInstance().getPlayer(player).disconnect(getBanMessage(reason, byname, time));
	      if (by != null)
	      {
	        ProxyServer.getInstance().getPlayer(by).sendMessage("§cJogador banido!");
	        return true;
	      }
	      ProxyServer.getInstance().getConsole().sendMessage("§cJogador banido!");
	      return true;
	    }
	    catch (SQLException e)
	    {
	      e.printStackTrace();
	      ProxyServer.getInstance().getConsole().sendMessage("§cUm erro aconteceu ao banir o jogador " + playername);
	    }
	    return false;
	  }
	  
	  public static boolean playerJaBanidoOnline(UUID player)
	  {
	    try
	    {
	      ResultSet rs = MySQLManager.db.querySQL("SELECT * FROM ban WHERE user= '" + player.toString() + "'");
	      if (rs.next()) {
	        return rs.getString("user") != null;
	      }
	    }
	    catch (SQLException|ClassNotFoundException e)
	    {
	      e.printStackTrace();
	      ProxyServer.getInstance().getConsole().sendMessage("§cUm erro aconteceu ao verificar o jogador " + player);
	    }
	    return false;
	  }
	  
	  public static boolean playerJaBanidoOffline(String player)
	  {
	    try
	    {
	      ResultSet rs = MySQLManager.db.querySQL("SELECT * FROM ban WHERE username= '" + player.toLowerCase() + "'");
	      if (rs.next()) {
	        return rs.getString("username") != null;
	      }
	    }
	    catch (SQLException|ClassNotFoundException e)
	    {
	      e.printStackTrace();
	      ProxyServer.getInstance().getConsole().sendMessage("§cUm erro aconteceu ao verificar o jogador " + player);
	    }
	    return false;
	  }
	  
	  public static String getBanMessage(String reason, String byname, long time)
	  {
	    if (time == -1L)
	    {
	      String msg = Main.getInstance().configs.mensagemQueOPlayerRecebeNaTelaAoSerBanidoPermanente.replaceAll("&", "§").replaceAll("%razao%", reason).replaceAll("%por%", byname).replaceAll("%linha%", "\n");
	      return msg;
	    }
	    String msg = Main.getInstance().getConfigs().mensagemQueOPlayerRecebeNaTelaAoSerBanidoTemporario.replaceAll("&", "§").replaceAll("%razao%", reason).replaceAll("%por%", byname).replaceAll("%linha%", "\n").replaceAll("%data%", ConvertMilliSecondsToFormattedDate(time)).replaceAll("%expira%", getHorasm(time - System.currentTimeMillis()));
	    return msg;
	  }
	  
	  public static long whenExpires(String playername)
	  {
	    playername = playername.toLowerCase();
	    long when = 0L;
	    try
	    {
	      ResultSet rs = MySQLManager.db.querySQL("SELECT * FROM ban WHERE username= '" + playername.toLowerCase() + "'");
	      if (rs.next()) {
	        when = rs.getLong("time");
	      }
	    }
	    catch (ClassNotFoundException|SQLException e)
	    {
	      e.printStackTrace();
	    }
	    return when;
	  }
	  
	  public static String getReason(String playername)
	  {
	    playername = playername.toLowerCase();
	    String reason;
	    try
	    {
	      ResultSet rs = MySQLManager.db
	        .querySQL("SELECT * FROM ban WHERE username= '" + playername.toLowerCase() + "'");
	      
	      if (rs.next()) {
	        reason = rs.getString("reason");
	      } else {
	        reason = "NOT FOUND";
	      }
	    }
	    catch (ClassNotFoundException|SQLException e)
	    {
	      
	      e.printStackTrace();
	      reason = "ERROR";
	    }
	    return reason;
	  }
	  
	  public static long getWhen(String playername)
	  {
	    long when = 0L;
	    try
	    {
	      ResultSet rs = MySQLManager.db.querySQL("SELECT * FROM ban WHERE username= '" + playername.toLowerCase() + "'");
	      if (rs.next()) {
	        when = rs.getLong("bannedin");
	      }
	    }
	    catch (ClassNotFoundException|SQLException e)
	    {
	      e.printStackTrace();
	    }
	    return when;
	  }
	  
	  public static String getBy(String playername)
	  {
	    playername = playername.toLowerCase();
	    String nome = null;
	    try
	    {
	      ResultSet rs = MySQLManager.db
	        .querySQL("SELECT * FROM ban WHERE username= '" + playername.toLowerCase() + "'");
	      if (rs.next()) {
	        nome = rs.getString("byplayer");
	      } else {
	        nome = "Not found";
	      }
	    }
	    catch (ClassNotFoundException|SQLException e)
	    {
	      e.printStackTrace();
	    }
	    return nome;
	  }
	  
	  public static boolean banOffline(String playername, UUID by, String byname, String reason, long time)
	  {
	    try
	    {
	      if (by != null) {
	        ProxyServer.getInstance().getPlayer(by).sendMessage("§aProcessando pedido!");
	      } else {
	        ProxyServer.getInstance().getConsole().sendMessage("§aProcessando pedido!");
	      }
	      boolean logou = MySQLManager.playerJaLogou(playername.toLowerCase());
	      if (logou)
	      {
	        String uuid = MySQLManager.getUUID(playername);
	        if (playerJaBanidoOffline(playername))
	        {
	          if (by != null) {
	            ProxyServer.getInstance().getPlayer(by).sendMessage("§cO jogador ja esta banido!");
	          } else {
	            ProxyServer.getInstance().getConsole().sendMessage("§cO jogador ja esta banido!");
	          }
	        }
	        else
	        {
	          PreparedStatement stm = MySQLManager.db.getConnection().prepareStatement(
	            "INSERT INTO `ban`(`user`, `username`, `byplayer`, `reason`, `time`, `bannedin`) VALUES (?, ?, ?, ?, ?, ?);");
	          stm.setString(1, uuid);
	          stm.setString(2, playername.toLowerCase());
	          if (by == null) {
	            stm.setString(3, "CONSOLE");
	          } else {
	            stm.setString(3, by.toString());
	          }
	          stm.setString(4, reason);
	          stm.setLong(5, time);
	          stm.setLong(6, System.currentTimeMillis());
	          stm.executeUpdate();
	          if (by != null)
	          {
	            ProxyServer.getInstance().getPlayer(by).sendMessage("§aJogador banido!");
	            return true;
	          }
	          ProxyServer.getInstance().getConsole().sendMessage("§aJogador banido");
	          return true;
	        }
	      }
	      else
	      {
	        if (by != null)
	        {
	          ProxyServer.getInstance().getPlayer(by).sendMessage("§cO jogador nunca entrou no servidor!");
	          return false;
	        }
	        ProxyServer.getInstance().getConsole().sendMessage("§cO jogador nunca entrou no servidor!");
	        return false;
	      }
	    }
	    catch (SQLException e)
	    {
	      e.printStackTrace();
	      ProxyServer.getInstance().getConsole().sendMessage("§cUm erro aconteceu ao banir o jogador " + playername);
	    }
	    return false;
	  }
	  
	  public static String ConvertMilliSecondsToFormattedDate(long time)
	  {
	    Date currentDate = new Date(time);
	    DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
	    return df.format(currentDate);
	  }
	  
	  public static String getHoras(long time)
	  {
	    int h = (int)(time / 1000L / 3600L);
	    int m = (int)(time / 1000L / 60L % 60L);
	    int s = (int)(time / 1000L % 60L);
	    return h + ":" + m + ":" + s;
	  }
	  
	  public static String getHorasm(long time)
	  {
	    long seconds = TimeUnit.MILLISECONDS.toSeconds(time);
	    int day = (int)TimeUnit.SECONDS.toDays(seconds);
	    long hours = TimeUnit.SECONDS.toHours(seconds) - 
	      TimeUnit.DAYS.toHours(day);
	    long minutes = TimeUnit.SECONDS.toMinutes(seconds) - 
	      TimeUnit.HOURS.toMinutes(TimeUnit.SECONDS.toHours(seconds));
	    long second = TimeUnit.SECONDS.toSeconds(seconds) - 
	      TimeUnit.MINUTES.toSeconds(TimeUnit.SECONDS.toMinutes(seconds));
	    
	    StringBuilder sb = new StringBuilder();
	    if (day > 0L) {
	      sb.append(day).append(" ").append("days").append(" ");
	    }
	    if (hours > 0L) {
	      sb.append(hours).append(" ").append("hours").append(" ");
	    }
	    if (minutes > 0L) {
	      sb.append(minutes).append(" ").append("minutes").append(" ");
	    }
	    if (second > 0L) {
	      sb.append(second).append(" ").append("seconds");
	    }
	    String diff = sb.toString();
	    
	    return diff.isEmpty() ? "0 seconds" : diff;
	  }
	}
