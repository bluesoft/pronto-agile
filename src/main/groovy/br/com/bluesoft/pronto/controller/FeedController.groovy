package br.com.bluesoft.pronto.controller

import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.oxm.xstream.XStreamMarshaller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.bluesoft.pronto.dao.MovimentoKanbanDao;
import br.com.bluesoft.pronto.dao.TicketDao;

import com.sun.syndication.feed.synd.SyndContent;
import com.sun.syndication.feed.synd.SyndContentImpl;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndEntryImpl;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.feed.synd.SyndFeedImpl;
import com.sun.syndication.io.SyndFeedOutput;

import static org.springframework.web.bind.annotation.RequestMethod.*

@Controller
@RequestMapping("/feed")
class FeedController {
	
	@Autowired
	TicketDao ticketDao
	
	@Autowired
	MovimentoKanbanDao movimentoKanbanDao
	
	@RequestMapping(value= '/tickets', method = GET)
	public void tickets(HttpServletResponse response) {
		
		def movimentos = movimentoKanbanDao.listarUltimosMovimentos()
		
		response.setContentType("text/xml");
		
	    final DateFormat DATE_PARSER = new SimpleDateFormat("yyyy-MM-dd");
		
		SyndFeed feed = new SyndFeedImpl()
		feed.setFeedType("atom_1.0")
		feed.setTitle("Pronto");
		feed.setDescription("Bla bla bla bla");
		feed.setAuthor "Lui"
		
		List entries = new ArrayList();
		SyndEntry entry;
		SyndContent description;
		
		movimentos.each { 
			entry = new SyndEntryImpl();
			entry.setTitle(it.ticket.ticketKey as String);
			entry.setLink("http://www.bluesoft.com.br/pronto/tickets/" + it.ticket.ticketKey);
			entry.setAuthor(it.usuario.username)
			
			entry.setPublishedDate(DATE_PARSER.parse("2004-06-08"));
			description = new SyndContentImpl();
			description.setType("text/plain");
			description.setValue(it.descricao);
			entry.setDescription(description);
			entries.add(entry);
			
			feed.setEntries(entries);
		}

        Writer writer = response.getWriter();
        SyndFeedOutput output = new SyndFeedOutput();
        output.output(feed,writer);
        writer.close();

		
		
	}
	
	@RequestMapping(value= '/usuario/{usuarioKey}', method = GET)
	String usuario() {
		null
	}
	
}