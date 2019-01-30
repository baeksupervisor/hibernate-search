package com.baeksupervisor.hibernatesearch;

import com.baeksupervisor.hibernatesearch.persistence.entity.Brand;
import com.baeksupervisor.hibernatesearch.persistence.entity.News;
import com.baeksupervisor.hibernatesearch.persistence.entity.Product;
import com.baeksupervisor.hibernatesearch.persistence.repository.BrandRepository;
import com.baeksupervisor.hibernatesearch.persistence.repository.NewsRepository;
import com.baeksupervisor.hibernatesearch.persistence.repository.ProductRepository;
import com.rometools.rome.feed.synd.SyndContentImpl;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ClassPathResource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@SpringBootApplication
public class HibernateSearchApplication implements CommandLineRunner {

	@Autowired
	private NewsRepository newsRepository;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private BrandRepository brandRepository;

	public static void main(String[] args) {
		SpringApplication.run(HibernateSearchApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		initNews();
		initProduct();
		initBrand();
	}

	private void initProduct() {

		ClassPathResource resource = new ClassPathResource("product_name.txt");

		try {
			Path path = Paths.get(resource.getURI());
			List<String> content = Files.readAllLines(path);
			List<Product> list = Files.readAllLines(path).stream().map(s -> new Product(s)).collect(Collectors.toList());
			productRepository.saveAll(list);
		} catch (IOException e) {
			log.error("{}", e.getMessage(), e);
		}
	}

	private void initBrand() {
		ClassPathResource resource = new ClassPathResource("brand_name.txt");

		try {
			Path path = Paths.get(resource.getURI());
			List<String> content = Files.readAllLines(path);
			List<Brand> list = Files.readAllLines(path).stream().map(s -> new Brand(s)).collect(Collectors.toList());
			brandRepository.saveAll(list);
		} catch (IOException e) {
			log.error("{}", e.getMessage(), e);
		}
	}

	private void initNews() throws Exception {
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

			Document document = Jsoup.parse(descriptionValue);
			Elements elements = document.getElementsByTag("img");
			String text = document.text();

//            log.info("title={}", title);
//            log.info("link={}", link);
//            log.info("description={}", descriptionValue);
//            log.info("elements={}", elements.attr("src"));
//            log.info("text={}", text);

			News news = new News();
			news.setTitle(title);
			news.setLink(link);
			news.setImage(elements.attr("src"));
			news.setDescription(text);

			newsList.add(news);
		}

		newsRepository.saveAll(newsList);
	}

	private String readFromInputStream(InputStream inputStream)
			throws IOException {
		StringBuilder resultStringBuilder = new StringBuilder();
		try (BufferedReader br
					 = new BufferedReader(new InputStreamReader(inputStream))) {
			String line;
			while ((line = br.readLine()) != null) {
				resultStringBuilder.append(line).append("\n");
			}
		}
		return resultStringBuilder.toString();
	}
}