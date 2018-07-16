package com.dnx.bans;

import java.sql.SQLException;

import com.dnx.bans.comandos.Ban;
import com.dnx.bans.comandos.BanInfo;
import com.dnx.bans.comandos.TempBan;
import com.dnx.bans.comandos.TempMute;
import com.dnx.bans.comandos.UnBan;
import com.dnx.bans.comandos.UnMute;
import com.dnx.bans.listeners.Entrar;
import com.dnx.bans.mysql.MySQLManager;

import net.cubespace.Yamler.Config.InvalidConfigurationException;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class Main extends Plugin{

	 public static Main instance;
	  public Configs configs;
	  
	  public void onEnable()
	  {
	    instance = this;
	    this.configs = new Configs(this);
	    try
	    {
	      this.configs.init();
	    }
	    catch (InvalidConfigurationException e)
	    {
	      ProxyServer.getInstance().getConsole().sendMessage("§cUm erro esta acontecendo com sua configura§§o!");
	      e.printStackTrace();
	    }
	    try
	    {
	      MySQLManager.setupDB();
	    }
	    catch (SQLException e)
	    {
	      debug("Um erro aconteceu ao conectar a mysql!");
	      e.printStackTrace();
	    }
	    new Entrar();
	    ProxyServer.getInstance().getPluginManager().registerCommand(this, new Ban("ban"));
	    ProxyServer.getInstance().getPluginManager().registerCommand(this, new UnBan("unban"));
	    ProxyServer.getInstance().getPluginManager().registerCommand(this, new TempBan("tempban"));
	    ProxyServer.getInstance().getPluginManager().registerCommand(this, new TempMute("tempmute"));
	    ProxyServer.getInstance().getPluginManager().registerCommand(this, new UnMute("unmute"));
	    ProxyServer.getInstance().getPluginManager().registerCommand(this, new BanInfo("baninfo"));
	  }
	  
	  public void onDisable() {}
	  
	  public static void debug(String msg)
	  {
	    ProxyServer.getInstance().getConsole().sendMessage("§c" + msg);
	  }
	  
	  public boolean tweet(String tweet)
	  {
	    ConfigurationBuilder cb = new ConfigurationBuilder();
	    cb.setDebugEnabled(true).setOAuthConsumerKey(this.configs.consumerKey)
	      .setOAuthConsumerSecret(this.configs.consumerKeySecret).setOAuthAccessToken(this.configs.accessToken)
	      .setOAuthAccessTokenSecret(this.configs.accessTokenSecret);
	    Twitter twitter = new TwitterFactory(cb.build()).getInstance();
	    try
	    {
	      twitter.updateStatus(tweet);
	      return true;
	    }
	    catch (TwitterException e)
	    {
	      e.printStackTrace();
	    }
	    return false;
	  }
	  
	  public Configs getConfigs()
	  {
	    return this.configs;
	  }
	  
	  public static Main getInstance()
	  {
	    return instance;
	  }
	}
