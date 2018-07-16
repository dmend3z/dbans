package com.dnx.bans.listeners;

import com.dnx.bans.Main;
import com.dnx.bans.api.BanAPI;
import com.dnx.bans.api.MuteAPI;
import com.dnx.bans.mysql.MySQLManager;

import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.PreLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class Entrar implements Listener
{
	  private final Main instance;
	  
	  public Entrar()
	  {
	    this.instance = Main.getInstance();
	    this.instance.getProxy().getPluginManager().registerListener(this.instance, this);
	  }
	  
	  @EventHandler
	  public void quandoPreLogar(PreLoginEvent e)
	  {
	    PendingConnection connection = e.getConnection();
	    String playername = connection.getName();
	    if (BanAPI.playerJaBanidoOffline(playername)) {
	      if (BanAPI.whenExpires(playername) == -1L)
	      {
	        e.setCancelReason(BanAPI.getBanMessage(BanAPI.getReason(playername), BanAPI.getBy(playername), BanAPI.whenExpires(playername)));
	        e.setCancelled(true);
	      }
	      else if (BanAPI.whenExpires(playername) < System.currentTimeMillis())
	      {
	        BanAPI.unban(playername, null);
	      }
	      else
	      {
	        e.setCancelReason(BanAPI.getBanMessage(BanAPI.getReason(playername), BanAPI.getBy(playername), BanAPI.whenExpires(playername)));
	        e.setCancelled(true);
	      }
	    }
	  }
	  
	  @EventHandler
	  public void quandoFalar(ChatEvent e)
	  {
	    ProxiedPlayer player = (ProxiedPlayer)e.getSender();
	    String playername = player.getName();
	    if (MuteAPI.playerJaMutadoOffline(playername)) {
	      if (MuteAPI.whenExpires(playername) == -1L)
	      {
	        player.sendMessage(MuteAPI.message(player));
	        e.setCancelled(true);
	      }
	      else if (MuteAPI.whenExpires(playername) < System.currentTimeMillis())
	      {
	        MuteAPI.unMute(playername, null);
	      }
	      else
	      {
	        player.sendMessage(MuteAPI.message(player));
	        e.setCancelled(true);
	      }
	    }
	  }
	  
	  @EventHandler
	  public void quandoLogar(PostLoginEvent e)
	  {
	    MySQLManager.criarJogador(e.getPlayer().getUniqueId());
	  }
	}