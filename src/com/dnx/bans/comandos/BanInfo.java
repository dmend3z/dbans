package com.dnx.bans.comandos;

import com.dnx.bans.Main;
import com.dnx.bans.api.BanAPI;
import com.dnx.bans.api.MuteAPI;
import com.dnx.bans.mysql.MySQLManager;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class BanInfo extends Command {

	public BanInfo(String name) {
		super(name);
	}

	@SuppressWarnings("deprecation")
	public void execute(CommandSender sender, String[] args) {
		if (!(sender instanceof ProxiedPlayer)) {
			if (args.length < 1) {
				sender.sendMessage("§cUso correto /baninfo <Player>");
				return;
			}
			ProxyServer.getInstance().getScheduler().runAsync(Main.getInstance(), new Runnable() {
				public void run() {
					if(MySQLManager.playerJaLogou(args[0])) {
						sender.sendMessage("§6==============");
						sender.sendMessage("§cNick: §e" + args[0]);
						sender.sendMessage("§cUUID: §e" + MySQLManager.getUUID(args[0]));
						sender.sendMessage("§cBanido: §e" + (BanAPI.playerJaBanidoOffline(args[0]) ? "Sim" : "Nao"));
						if(BanAPI.playerJaBanidoOffline(args[0])) {
							sender.sendMessage("§cRaz§o do Ban: §e" + BanAPI.getReason(args[0]));
							sender.sendMessage("§cBanido por: §e" + BanAPI.getBy(args[0]));
							if(BanAPI.whenExpires(args[0]) == -1) {
								sender.sendMessage("§cExpira em: §e" + "Nunca");
							}else{
								sender.sendMessage("§cExpira em: §e" + BanAPI.getHorasm(BanAPI.whenExpires(args[0]) - System.currentTimeMillis()));
							}
						}
						sender.sendMessage("§cMutado: §e" + (MuteAPI.playerJaMutadoOffline(args[0]) ? "Sim":"Nao"));
						if(MuteAPI.playerJaMutadoOffline(args[0])) {
							sender.sendMessage("§cRazao do Mute: §e" + MuteAPI.getReason(args[0]));
							sender.sendMessage("§cMutado por: §e" + MuteAPI.getBy(args[0]));
							if(MuteAPI.whenExpires(args[0]) == -1) {
								sender.sendMessage("§cExpira em: §e" + "Nunca");
							}else{
								sender.sendMessage("§cExpira em: §e" + MuteAPI.getHorasm(MuteAPI.whenExpires(args[0]) - System.currentTimeMillis()));
							}
						}
						sender.sendMessage("§6==============");
					}else{
						sender.sendMessage("§cO jogador nunca entrou no servidor!");
					}
				}
			});
			return;
		}
		ProxiedPlayer by = (ProxiedPlayer) sender;
		if (!sender.hasPermission("dbans.baninfo")) {
			sender.sendMessage(Main.getInstance().configs.mensagemDeNaoTemPermissaoParaComando.replaceAll("&", "§"));
			return;
		}
		if (args.length < 1) {
			by.sendMessage("§cUso correto: /baninfo <Player>");
			return;
		}
		ProxyServer.getInstance().getScheduler().runAsync(Main.getInstance(), new Runnable() {
			public void run() {
				
				if(MySQLManager.playerJaLogou(args[0])) {
					sender.sendMessage("§6==============");
					sender.sendMessage("§cNick: §e" + args[0]);
					sender.sendMessage("§cUUID: §e" + MySQLManager.getUUID(args[0]));
					sender.sendMessage("§cBanido: §e" + (BanAPI.playerJaBanidoOffline(args[0]) ? "Sim" : "Nao"));
					if(BanAPI.playerJaBanidoOffline(args[0])) {
						sender.sendMessage("§cRazao do Ban: §e" + BanAPI.getReason(args[0]));
						sender.sendMessage("§cBanido por: §e" + BanAPI.getBy(args[0]));
						if(BanAPI.whenExpires(args[0]) == -1) {
							sender.sendMessage("§cExpira em: §e" + "Nunca");
						}else{
							sender.sendMessage("§cExpira em: §e" + BanAPI.getHorasm(BanAPI.whenExpires(args[0]) - System.currentTimeMillis()));
						}
					}
					sender.sendMessage("§cMutado: §e" + (MuteAPI.playerJaMutadoOffline(args[0]) ? "Sim":"Nao"));
					if(MuteAPI.playerJaMutadoOffline(args[0])) {
						sender.sendMessage("§cRazao do Mute: §e" + MuteAPI.getReason(args[0]));
						sender.sendMessage("§cMutado por: §e" + MuteAPI.getBy(args[0]));
						if(MuteAPI.whenExpires(args[0]) == -1) {
							sender.sendMessage("§cExpira em: §e" + "Nunca");
						}else{
							sender.sendMessage("§cExpira em: §e" + MuteAPI.getHorasm(MuteAPI.whenExpires(args[0]) - System.currentTimeMillis()));
						}
					}
					sender.sendMessage("§6==============");
				}else{
					sender.sendMessage("§cO jogador nunca entrou no servidor!");
				}
				
			}
		});
	}

}
