package com.dnx.bans.comandos;

import java.util.concurrent.TimeUnit;

import com.dnx.bans.Main;
import com.dnx.bans.api.MuteAPI;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class TempMute extends Command {

	public TempMute(String name) {
		super(name);
	}

	@SuppressWarnings("deprecation")
	public void execute(CommandSender sender, String[] args) {
		if (!(sender instanceof ProxiedPlayer)) {
			if (args.length < 2) {
				sender.sendMessage("§cUso correto /tempmute <Player> <Tempo em minutos> <Reason>");
				return;
			}
			ProxyServer.getInstance().getScheduler().runAsync(Main.getInstance(), new Runnable() {
				public void run() {
					if (ProxyServer.getInstance().getPlayer(args[0]) != null) {
						ProxiedPlayer target = ProxyServer.getInstance().getPlayer(args[0]);
						StringBuilder strBuilder = new StringBuilder();
						for (int i = 2; i < args.length; i++) {
							strBuilder.append(args[i]);
							strBuilder.append(" ");
						}
						String msg = strBuilder.toString();
						if(isInteger(args[1])) {
							int time = Integer.parseInt(args[1]);
							long tempoEmMilli = TimeUnit.MINUTES.toMillis(time);
							long tempofinal = (TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis())) + (TimeUnit.MILLISECONDS.toSeconds(tempoEmMilli));
							if(MuteAPI.banOnline(target.getUniqueId(), target.getName(), null, "CONSOLE", msg, TimeUnit.SECONDS.toMillis(tempofinal))) {
								for (ProxiedPlayer p : ProxyServer.getInstance().getPlayers()) {
									if(p.hasPermission("dbans.tempmute")) {
										p.sendMessage(Main.getInstance().configs.mensagemAoEfetuarOMuteTemporario.replaceAll("%player%", args[0]).replaceAll("%por%", "CONSOLA").replaceAll("%razao%", "msg").replaceAll("%tempo%", MuteAPI.getHorasm(tempoEmMilli)).replaceAll("&", "§"));
									}
								}
							}
							/*MuteAPI.banOnline(target.getUniqueId(), target.getName(), null, "CONSOLE", msg, TimeUnit.SECONDS.toMillis(tempofinal));
							for (ProxiedPlayer p : ProxyServer.getInstance().getPlayers()) {
								if(p.hasPermission("dbans.tempmute")) {
									p.sendMessage("§b§lTEMPMUTE §f"+target.getName() + " has been §b§lTEMPORARILY MUTED§f for " + MuteAPI.getHorasm(tempoEmMilli) + " by CONSOLE");
								}
							}*/
						}else{
							ProxyServer.getInstance().getConsole().sendMessage("§cUse numeros para o tempo!");
						}
					} else {
						StringBuilder strBuilder = new StringBuilder();
						for (int i = 2; i < args.length; i++) {
							strBuilder.append(args[i]);
							strBuilder.append(" ");
						}
						String msg = strBuilder.toString();
						if(isInteger(args[1])) {
							int time = Integer.parseInt(args[1]);
							long tempoEmMilli = TimeUnit.MINUTES.toMillis(time);
							long tempofinal = (TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis())) + (TimeUnit.MILLISECONDS.toSeconds(tempoEmMilli));
							if(MuteAPI.banOffline(args[0], null, "CONSOLE", msg, TimeUnit.SECONDS.toMillis(tempofinal))) {
								for (ProxiedPlayer p : ProxyServer.getInstance().getPlayers()) {
									if(p.hasPermission("dbans.tempmute")) {
										p.sendMessage(Main.getInstance().configs.mensagemAoEfetuarOMuteTemporario.replaceAll("%player%", args[0]).replaceAll("%por%", "CONSOLA").replaceAll("%razao%", "msg").replaceAll("%tempo%", MuteAPI.getHorasm(tempoEmMilli)).replaceAll("&", "§"));
									}
								}
							}
							/*MuteAPI.banOffline(args[0], null, "CONSOLE", msg, TimeUnit.SECONDS.toMillis(tempofinal));
							for (ProxiedPlayer p : ProxyServer.getInstance().getPlayers()) {
								if(p.hasPermission("dbans.tempmute")) {
									p.sendMessage("§b§lTEMPMUTE §f"+args[0] + " has been §b§lTEMPORARILY MUTED§f for " + MuteAPI.getHorasm(tempoEmMilli) + " by CONSOLE");
								}
							}*/
						}else{
							ProxyServer.getInstance().getConsole().sendMessage("§cUse numeros para o tempo!");
						}
					}	
				}
			});
			return;
		}
		ProxiedPlayer by = (ProxiedPlayer) sender;
		if (!sender.hasPermission("dbans.tempmute")) {
			sender.sendMessage(Main.getInstance().getConfigs().mensagemDeNaoTemPermissaoParaComando.replaceAll("&", "§"));
			return;
		}
		if (args.length < 2) {
			by.sendMessage("§cUso correto: /tempmute <Player> <Tempo em minutos> <Razao>");
			return;
		}
		ProxyServer.getInstance().getScheduler().runAsync(Main.getInstance(), new Runnable() {
			public void run() {
				ProxiedPlayer target = ProxyServer.getInstance().getPlayer(args[0]);
				if (target != null) {
					StringBuilder strBuilder = new StringBuilder();
					for (int i = 2; i < args.length; i++) {
						strBuilder.append(args[i]);
						strBuilder.append(" ");
					}
					String msg = strBuilder.toString();
					if(isInteger(args[1])) {
						int time = Integer.parseInt(args[1]);
						long tempoEmMilli = TimeUnit.MINUTES.toMillis(time);
						long tempofinal = (TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis())) + (TimeUnit.MILLISECONDS.toSeconds(tempoEmMilli));
						if(MuteAPI.banOnline(target.getUniqueId(), target.getName(), by.getUniqueId(), by.getName(), msg, TimeUnit.SECONDS.toMillis(tempofinal))) {
							for (ProxiedPlayer p : ProxyServer.getInstance().getPlayers()) {
								if(p.hasPermission("dbans.tempmute")) {
									p.sendMessage(Main.getInstance().configs.mensagemAoEfetuarOMuteTemporario.replaceAll("%player%", args[0]).replaceAll("%por%", by.getName()).replaceAll("%razao%", "msg").replaceAll("%tempo%", MuteAPI.getHorasm(tempoEmMilli)).replaceAll("&", "§"));
								}
							}
						}
						/*MuteAPI.banOnline(target.getUniqueId(), target.getName(), by.getUniqueId(), by.getName(), msg, TimeUnit.SECONDS.toMillis(tempofinal));
						for (ProxiedPlayer p : ProxyServer.getInstance().getPlayers()) {
							if(p.hasPermission("dbans.tempmute")) {
								p.sendMessage("§b§lTEMPMUTE §f"+target.getName() + " has been §b§lTEMPORARILY MUTED§f for " + MuteAPI.getHorasm(tempoEmMilli) + " by " + by.getName());
							}
						}*/
					}else{
						by.sendMessage("§cUse numeros para banir temporariamente");
					}
				} else {
					StringBuilder strBuilder = new StringBuilder();
					for (int i = 2; i < args.length; i++) {
						strBuilder.append(args[i]);
						strBuilder.append(" ");
					}
					String msg = strBuilder.toString();
					if(isInteger(args[1])) {
						int time = Integer.parseInt(args[1]);
						long tempoEmMilli = TimeUnit.MINUTES.toMillis(time);
						long tempofinal = (TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis())) + (TimeUnit.MILLISECONDS.toSeconds(tempoEmMilli));
						if(MuteAPI.banOffline(args[0], by.getUniqueId(), by.getName(), msg, TimeUnit.SECONDS.toMillis(tempofinal))) {
							for (ProxiedPlayer p : ProxyServer.getInstance().getPlayers()) {
								if(p.hasPermission("dbans.tempmute")) {
									p.sendMessage(Main.getInstance().configs.mensagemAoEfetuarOMuteTemporario.replaceAll("%player%", args[0]).replaceAll("%por%", by.getName()).replaceAll("%razao%", "msg").replaceAll("%tempo%", MuteAPI.getHorasm(tempoEmMilli)).replaceAll("&", "§"));
								}
							}
						}
						/*MuteAPI.banOffline(args[0], by.getUniqueId(), by.getName(), msg, TimeUnit.SECONDS.toMillis(tempofinal));
						for (ProxiedPlayer p : ProxyServer.getInstance().getPlayers()) {
							if(p.hasPermission("dbans.tempmute")) {
								p.sendMessage("§b§lTEMPMUTE §f"+args[0] + " has been §b§lTEMPORARILY MUTED§f for " + MuteAPI.getHorasm(tempoEmMilli) + " by " + by.getName());
							}
						}*/
					}else{
						by.sendMessage("§cUse numeros para banir temporariamente");
					}
				}	
			}
		});
	}
	
	public boolean isInteger(String s) {
	    try { 
	        Integer.parseInt(s); 
	    } catch(NumberFormatException e) { 
	        return false; 
	    } catch(NullPointerException e) {
	        return false;
	    }
	    return true;
	}

}