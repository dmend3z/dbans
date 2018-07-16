package com.dnx.bans.comandos;

import java.util.Iterator;
import java.util.concurrent.TimeUnit;

import com.dnx.bans.Main;
import com.dnx.bans.api.BanAPI;
import com.dnx.bans.api.MuteAPI;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class TempBan extends Command
{
	  public TempBan(String name)
	  {
	    super(name);
	  }
	  
	  public void execute(CommandSender sender, final String[] args)
	  {
	    if (!(sender instanceof ProxiedPlayer))
	    {
	      if (args.length < 2)
	      {
	        sender.sendMessage("§cUso correto /tempban <Player> <Tempo em minutos> <Reason>");
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
	            for (int i = 2; i < args.length; i++)
	            {
	              strBuilder.append(args[i]);
	              strBuilder.append(" ");
	            }
	            String msg = strBuilder.toString();
	            if (TempBan.this.isInteger(args[1]))
	            {
	              int time = Integer.parseInt(args[1]);
	              long tempoEmMilli = TimeUnit.MINUTES.toMillis(time);
	              long tempofinal = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()) + TimeUnit.MILLISECONDS.toSeconds(tempoEmMilli);
	              if (BanAPI.banOnline(target.getUniqueId(), target.getName(), null, "CONSOLE", msg, TimeUnit.SECONDS.toMillis(tempofinal))) {
	                for (Iterator localIterator = ProxyServer.getInstance().getPlayers().iterator(); localIterator.hasNext();)
	                {
	                  p = (ProxiedPlayer)localIterator.next();
	                  if (p.hasPermission("dbans.tempban")) {
	                    p.sendMessage(Main.getInstance().configs.mensagemAoEfetuarOMute.replaceAll("%player%", target.getName()).replaceAll("%por%", "CONSOLA").replaceAll("%razao%", "msg").replaceAll("%tempo%", MuteAPI.getHorasm(tempoEmMilli)).replaceAll("&", "§"));
	                  }
	                }
	              }
	            }
	            else
	            {
	              ProxyServer.getInstance().getConsole().sendMessage("§cUse numeros para o tempo!");
	            }
	          }
	          else
	          {
	            StringBuilder strBuilder = new StringBuilder();
	            for (int i = 2; i < args.length; i++)
	            {
	              strBuilder.append(args[i]);
	              strBuilder.append(" ");
	            }
	            String msg = strBuilder.toString();
	            if (TempBan.this.isInteger(args[1]))
	            {
	              int time = Integer.parseInt(args[1]);
	              long tempoEmMilli = TimeUnit.MINUTES.toMillis(time);
	              long tempofinal = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()) + TimeUnit.MILLISECONDS.toSeconds(tempoEmMilli);
	              if (BanAPI.banOffline(args[0], null, "CONSOLE", msg, TimeUnit.SECONDS.toMillis(tempofinal))) {
	                for (ProxiedPlayer p1 : ProxyServer.getInstance().getPlayers()) {
	                  if (p1.hasPermission("dbans.tempban")) {
	                    p1.sendMessage(Main.getInstance().configs.mensagemAoEfetuarOMute.replaceAll("%player%", args[0]).replaceAll("%por%", "CONSOLA").replaceAll("%razao%", "msg").replaceAll("%tempo%", BanAPI.getHorasm(tempoEmMilli)).replaceAll("&", "§"));
	                  }
	                }
	              }
	            }
	            else
	            {
	              ProxyServer.getInstance().getConsole().sendMessage("§cUse numeros para o tempo!");
	            }
	          }
	        }
	      });
	      return;
	    }
	    final ProxiedPlayer by = (ProxiedPlayer)sender;
	    if (!sender.hasPermission("dbans.tempban"))
	    {
	      sender.sendMessage(Main.getInstance().getConfigs().mensagemDeNaoTemPermissaoParaComando.replaceAll("&", "§"));
	      return;
	    }
	    if (args.length < 2)
	    {
	      by.sendMessage("§cUso correto: /tempban <Player> <Tempo em minutos> <Raz§o>");
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
	          for (int i = 2; i < args.length; i++)
	          {
	            strBuilder.append(args[i]);
	            strBuilder.append(" ");
	          }
	          String msg = strBuilder.toString();
	          if (TempBan.this.isInteger(args[1]))
	          {
	            int time = Integer.parseInt(args[1]);
	            long tempoEmMilli = TimeUnit.MINUTES.toMillis(time);
	            long tempofinal = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()) + TimeUnit.MILLISECONDS.toSeconds(tempoEmMilli);
	            if (BanAPI.banOnline(target.getUniqueId(), target.getName(), by.getUniqueId(), by.getName(), msg, TimeUnit.SECONDS.toMillis(tempofinal))) {
	              for (ProxiedPlayer p : ProxyServer.getInstance().getPlayers()) {
	                if (p.hasPermission("sbans.tempban")) {
	                  p.sendMessage(Main.getInstance().configs.mensagemAoEfetuarOMute.replaceAll("%player%", target.getName()).replaceAll("%por%", by.getName()).replaceAll("%razao%", "msg").replaceAll("%tempo%", BanAPI.getHorasm(tempoEmMilli)).replaceAll("&", "§"));
	                }
	              }
	            }
	          }
	          else
	          {
	            by.sendMessage("§cUse numeros para banir o jogador temporariamente");
	          }
	        }
	        else
	        {
	          StringBuilder strBuilder = new StringBuilder();
	          for (int i = 2; i < args.length; i++)
	          {
	            strBuilder.append(args[i]);
	            strBuilder.append(" ");
	          }
	          String msg = strBuilder.toString();
	          if (TempBan.this.isInteger(args[1]))
	          {
	            int time = Integer.parseInt(args[1]);
	            long tempoEmMilli = TimeUnit.MINUTES.toMillis(time);
	            long tempofinal = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()) + TimeUnit.MILLISECONDS.toSeconds(tempoEmMilli);
	            if (BanAPI.banOffline(args[0], by.getUniqueId(), by.getName(), msg, TimeUnit.SECONDS.toMillis(tempofinal))) {
	              for (ProxiedPlayer p : ProxyServer.getInstance().getPlayers()) {
	                if (p.hasPermission("dbans.tempban")) {
	                  p.sendMessage(Main.getInstance().configs.mensagemAoEfetuarOMute.replaceAll("%player%", args[0]).replaceAll("%por%", by.getName()).replaceAll("%razao%", "msg").replaceAll("%tempo%", BanAPI.getHorasm(tempoEmMilli)).replaceAll("&", "§"));
	                }
	              }
	            }
	          }
	          else
	          {
	            by.sendMessage("§cUse numeros para banir o jogador temporariamente");
	          }
	        }
	      }
	    });
	  }
	  
	  public boolean isInteger(String s)
	  {
	    try
	    {
	      Integer.parseInt(s);
	    }
	    catch (NumberFormatException e)
	    {
	      return false;
	    }
	    catch (NullPointerException e)
	    {
	      return false;
	    }
	    return true;
	  }

}
