package br.com.bluesoft.pronto.service

import javax.annotation.PostConstruct

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSenderImpl
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service

import br.com.bluesoft.pronto.dao.ConfiguracaoDao
import br.com.bluesoft.pronto.model.Usuario

@Service
class MailMessageService implements MessageService {

	@Autowired ConfiguracaoDao configuracaoDao
	@Autowired JavaMailSenderImpl mailSender;

	public void configure() {
			mailSender.setHost(configuracaoDao.getMailHost());
			mailSender.setUsername(configuracaoDao.getMailUserName());
			mailSender.setPassword(configuracaoDao.getMailPassword());
			mailSender.setPort(configuracaoDao.getMailPort());
			mailSender.setProtocol(configuracaoDao.getMailProtocol());
			mailSender.setDefaultEncoding("UTF-8");

			Properties props = new Properties();
			props.put("mail.smtp.auth", String.valueOf(configuracaoDao.isMailAuth()));
			props.put("mail.smtp.starttls.enable", String.valueOf(configuracaoDao.isMailTls()));
			mailSender.setJavaMailProperties(props)
	}


	@Async
	public boolean enviarMensagem(String subject, String msg, def to) {
		if (configuracaoDao.isMailNotificationAtivo()) {
			configure();
			try {
				to.each { Usuario usuario ->
					if (usuario) {
						enviar(subject, msg, usuario.email);
					}
				}
				return true
			} catch(Throwable e) {
				e.printStackTrace()
				return false
			}
		}
		return false
	}

	private void enviar(String subject, String body, String to){
		SimpleMailMessage msg = new SimpleMailMessage();
		msg.setFrom("nfe-cosmos@bluesoft.com.br");
		msg.setTo(to);
		msg.setText(body);
		msg.setSubject("Pronto - " + subject);
		this.mailSender.send(msg);
	}
}
