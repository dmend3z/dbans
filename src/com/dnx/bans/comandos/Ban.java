package com.dnx.bans.comandos;

import java.util.Iterator;

import com.dnx.bans.Main;
import com.dnx.bans.api.BanAPI;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class Ban extends Command
{
	  public Ban(String name)
	  {
	    super(name);
	  }
	  
	  public void execute(CommandSender sender, final String[] args)
	  {
	    if (!(sender instanceof ProxiedPlayer))
	    {
	      if (args.length < 2)
	      {
	        sender.sendMessage("§cUso correto /ban <Player> <Reason>");
	        return;
	      }
	      ProxyServer.getInstance().getScheduler().runAsync(Main.getInstance(), new Runnable()
	      {
	        public void run()
	        {
	          ProxiedPlayer p;
	          if (ProxyServer.getInstance().getPlayer(args[0]) != null)
	          {
	            ProxiedPlayer target = ProxyServer.getInstance().getPlayer(args[0]);
	            StringBuilder strBuilder = new StringBuilder();
	            for (int i = 1; i < args.length; i++)
	            {
	              strBuilder.append(args[i]);
	              strBuilder.append(" ");
	            }
	            String msg = strBuilder.toString();
	            if (BanAPI.banOnline(target.getUniqueId(), target.getName(), null, "CONSOLE", msg, -1L))
	            {
	              for (Iterator localIterator = ProxyServer.getInstance().getPlayers().iterator(); localIterator.hasNext();)
	              {
	                p = (ProxiedPlayer)localIterator.next();
	                if (p.hasPermission("dbans.ban")) {
	                  p.sendMessage(Main.getInstance().configs.mensagemDeBan
	                    .replaceAll("%player%", target.getName()).replaceAll("%por%", "CONSOLA")
	                    .replaceAll("%razao%", "msg").replaceAll("&", "§"));
	                }
	              }
	              if (Main.getInstance().configs.broadcastBan) {
	                Main.getInstance().tweet("O jogador " + target.getName() + 
	                  " foi banido permanentemente!\n\nRaz§o: " + msg + "\nPor: CONSOLE");
	              }
	            }
	          }
	          else
	          {
	            StringBuilder strBuilder = new StringBuilder();
	            for (int i = 1; i < args.length; i++)
	            {
	              strBuilder.append(args[i]);
	              strBuilder.append(" ");
	            }
	            String msg = strBuilder.toString();
	            if (BanAPI.banOffline(args[0], null, "CONSOLE", msg, -1L))
	            {
	              for (ProxiedPlayer p1 : ProxyServer.getInstance().getPlayers()) {
	                if (p1.hasPermission("dbans.ban")) {
	                  p1.sendMessage(Main.getInstance().configs.mensagemDeBan
	                    .replaceAll("%player%", args[0]).replaceAll("%por%", "CONSOLA")
	                    .replaceAll("%razao%", "msg").replaceAll("&", "§"));
	                }
	              }
	              if (Main.getInstance().getConfigs().broadcastBan) {
	                Main.getInstance().tweet("? O jogador " + args[0] + 
	                  " foi banido permanentemente!\n\nRazao§o: " + msg + "\nPor: CONSOLE");
	              }
	            }
	          }
	        }
	      });
	      return;
	    }
	    final ProxiedPlayer by = (ProxiedPlayer)sender;
	    if (!sender.hasPermission("dbans.ban"))
	    {
	      sender.sendMessage(Main.getInstance().configs.mensagemDeNaoTemPermissaoParaComando.replaceAll("&", "§"));
	      return;
	    }
	    if (args.length < 2)
	    {
	      by.sendMessage("§cUso correto: /ban <Player> <Razao>");
	      return;
	    }
	    ProxyServer.getInstance().getScheduler().runAsync(Main.getInstance(), new Runnable()
	    {
	      public void run()
	      {
	        ProxiedPlayer target = ProxyServer.getInstance().getPlayer(args[0]);
	        if (target != null)
	        {
	          StringBuilder strBuilder = new StringBuilder();
	          for (int i = 1; i < args.length; i++)
	          {
	            strBuilder.append(args[i]);
	            strBuilder.append(" ");
	          }
	          String msg = strBuilder.toString();
	          if (BanAPI.banOnline(target.getUniqueId(), target.getName(), by.getUniqueId(), by.getName(), msg, -1L))
	          {
	            for (ProxiedPlayer p : ProxyServer.getInstance().getPlayers()) {
	              if (p.hasPermission("dbans.ban")) {
	                p.sendMessage(Main.getInstance().configs.mensagemDeBan
	                  .replaceAll("%player%", target.getName()).replaceAll("%por%", by.getName())
	                  .replaceAll("%razao%", "msg").replaceAll("&", "§"));
	              }
	            }
	            if (Main.getInstance().getConfigs().broadcastBan) {
	              Main.getInstance().tweet("? O jogador " + target.getName() + 
	                " foi banido permanentemente!\n\nRazao: " + msg + "\nPor: " + by.getName());
	            }
	          }
	        }
	        else
	        {
	          StringBuilder strBuilder = new StringBuilder();
	          for (int i = 1; i < args.length; i++)
	          {
	            strBuilder.append(args[i]);
	            strBuilder.append(" ");
	          }
	          String msg = strBuilder.toString();
	          if (BanAPI.banOffline(args[0], by.getUniqueId(), by.getName(), msg, -1L))
	          {
	            for (ProxiedPlayer p : ProxyServer.getInstance().getPlayers()) {
	              if (p.hasPermission("dbans.ban")) {
	                p.sendMessage(Main.getInstance().configs.mensagemDeBan.replaceAll("%player%", args[0])
	                  .replaceAll("%por%", by.getName()).replaceAll("%razao%", "msg")
	                  .replaceAll("&", "§"));
	              }
	            }
	            if (Main.getInstance().getConfigs().broadcastBan) {
	              Main.getInstance().tweet("? O jogador " + args[0] + 
	                " foi banido permanentemente!\n\nRazao: " + msg + "\nPor: " + by.getName());
	            }
	          }
	        }
	      }
	    });
	  }
	}