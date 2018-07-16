package com.dnx.bans;

import java.io.File;

import net.cubespace.Yamler.Config.Comment;
import net.cubespace.Yamler.Config.Comments;
import net.cubespace.Yamler.Config.Config;
import net.md_5.bungee.api.plugin.Plugin;

@SuppressWarnings("deprecation")
public class Configs extends Config {

	public Configs(Plugin plugin) {
		CONFIG_HEADER = new String[] { "Configuração do plugin de banimentos, feito por Soziks" };
		CONFIG_FILE = new File(plugin.getDataFolder(), "config.yml");
	}
	
	public String mensagemDeNaoTemPermissaoParaComando = "&cDesculpe porém não tem permissão para este comando!";
	
	@Comment("Aqui você irá decidir se quer que o plugin faça broadcast no twitter, true para fazer, false para não fazer")
	public boolean broadcastBan = false;
	
	@Comment("Aqui você irá colocar a ConsumerKey, pode ser encontrada no manual de instruções")
	public String consumerKey = "ConsumerKey";
	
	@Comment("Aqui você irá colocar a ConsumerKeySecret, pode ser encontrada no manual de instruções")
	public String consumerKeySecret = "ConsumerKeySecret";
	
	@Comment("Aqui você irá colocar a accessToken, pode ser encontrada no manual de instruções")
	public String accessToken = "accessToken";
	
	@Comment("Aqui você irá colocar a accessTokenSecret, pode ser encontrada no manual de instruções")
	public String accessTokenSecret = "accessTokenSecret";
	
	@Comment("Aqui você irá colocar as informações da mysql!")
	public String hostname = "hostname";
	public String port = "3306";
	public String database = "database";
	public String userName = "username";
	public String password = "password";
	@Comments({
		"Nesta razão você pode usar varias variaveis",
		"Use %por% para automaticamente ser colocado o nome do jogador que mutou, pode não colocar também",
		"Use %razao% para automaticamente ser colocado a razão do mute, pode não colocar também"
	})
	public String mensagemDeMute = "&aVocê foi mutado por %por% pela razão de %razao%";
	@Comments({
		"Nesta razão você pode usar varias variaveis",
		"Não coloque uma mensagem longa, pois caso o mute seja temporario esta mensagem será mostrada depois da mensagemDeMute!",
		"Use %tempo% para automaticamente ser colocado o tempo do mute, pode não colocar também"
	})
	public String mensagemDeMuteTemporario = "&aO seu mute expira em %tempo%";
	@Comments({
		"Nesta razão você pode usar varias variaveis",
		"Não coloque uma mensagem longa, pois caso o mute seja permanente esta mensagem será mostrada depois da mensagemDeMute!",
	})
	public String mensagemDeMutePermanente = "&aO seu mute é permanente!";
	@Comments({
		"Nesta mensagem você pode usar varias variaveis",
		"Use %player% para automaticamente ser colocado o nome do jogador banido, pode não colocar também",
		"Use %por% para automaticamente ser colocado o nome do jogador que baniu, pode não colocar também",
		"Use %razao% para automaticamente ser colocado a razão do ban, pode não colocar também",
	})
	public String mensagemDeBan = "&cO jogador %player% foi banido permanentemente por %por%";
	@Comments({
		"Nesta mensagem você pode usar varias variaveis",
		"Use %player% para automaticamente ser colocado o nome do jogador banido, pode não colocar também",
		"Use %por% para automaticamente ser colocado o nome do jogador que baniu, pode não colocar também",
		"Use %razao% para automaticamente ser colocado a razão do ban, pode não colocar também",
	})
	public String mensagemAoEfetuarOMute = "&cO jogador %player% foi mutado permanentemente por %por%";
	@Comments({
		"Nesta mensagem você pode usar varias variaveis",
		"Use %player% para automaticamente ser colocado o nome do jogador banido, pode não colocar também",
		"Use %por% para automaticamente ser colocado o nome do jogador que baniu, pode não colocar também",
		"Use %razao% para automaticamente ser colocado a razão do ban, pode não colocar também",
		"Use %tempo% para automaticamente ser colocado o tempo do tempmute, pode nao colocar também" 
	})
	public String mensagemAoEfetuarOMuteTemporario = "&cO jogador %player% foi mutado temporariamente por %por%";

	@Comments({
		"Nesta mensagem você pode usar varias variaveis",
		"Use %linha% para colocar uma linha nova, pode não colocar também",
		"Use %razao% para colocar a razão do ban, pode não colocar também",
		"Use %por% para colocar quem baniu o jogador, pode não colocar também"
	})
	public String mensagemQueOPlayerRecebeNaTelaAoSerBanidoPermanente = "&7Você foi &c&lBANIDO&r %linha% %linha%&cRazão:%razao%%linha%&cPor:%por%";


	@Comments({
		"Nesta mensagem você pode usar varias variaveis",
		"Use %linha% para colocar uma linha nova, pode não colocar também",
		"Use %razao% para colocar a razão do ban, pode não colocar também",
		"Use %por% para colocar quem baniu o jogador, pode não colocar também",
		"Use %data% para colocar a data de quando vai expirar o ban, pode não colocar também",
		"Use %expira% para colocar quanto tempo para expirar, pode não colocar também"
	})
	public String mensagemQueOPlayerRecebeNaTelaAoSerBanidoTemporario = "&7Você foi &c&lTEMPORARIAMENTE BANIDO&r %linha% %linha%&cRazão:%razao%%linha%&cPor:%por%%linha%&cO ban acaba na data: %data%%linha%&cExpira em: %expira%";
	
}
