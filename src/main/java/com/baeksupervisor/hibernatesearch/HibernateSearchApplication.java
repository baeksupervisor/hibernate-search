package com.baeksupervisor.hibernatesearch;

import com.baeksupervisor.hibernatesearch.persistence.entity.News;
import com.baeksupervisor.hibernatesearch.persistence.repository.NewsRepository;
import com.rometools.rome.feed.synd.SyndContentImpl;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Slf4j
@SpringBootApplication
public class HibernateSearchApplication implements CommandLineRunner {

	@Autowired
	private NewsRepository newsRepository;

	public static void main(String[] args) {
		SpringApplication.run(HibernateSearchApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		String url = "http://rss.donga.com/total.xml";
		SyndFeed feed = new SyndFeedInput().build(new XmlReader(new URL(url)));

		List<News> newsList = new ArrayList<>();

		Iterator<SyndEntry> it = feed.getEntries().iterator();
		while(it.hasNext()) {
			SyndEntry entry = it.next();
			String title = entry.getTitle();
			String link = entry.getLink();

			SyndContentImpl content = (SyndContentImpl) entry.getDescription();
			String descriptionValue = content.getValue();

//            log.info("title={}", title);
//            log.info("link={}", link);
//            log.info("description={}", descriptionValue);

			News news = new News();
			news.setTitle(title);
			news.setLink(link);
			news.setDescription(descriptionValue);

			newsList.add(news);
		}


		newsRepository.saveAll(newsList);
	}
}