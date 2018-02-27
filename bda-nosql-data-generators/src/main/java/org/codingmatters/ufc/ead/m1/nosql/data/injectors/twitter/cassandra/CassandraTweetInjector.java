package org.codingmatters.ufc.ead.m1.nosql.data.injectors.twitter.cassandra;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.Session;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.codingmatters.ufc.ead.m1.nosql.data.injectors.InjectorException;
import org.codingmatters.ufc.ead.m1.nosql.data.injectors.twitter.InjectionException;
import org.codingmatters.ufc.ead.m1.nosql.data.injectors.twitter.TweetInjector;
import org.codingmatters.ufc.ead.m1.nosql.data.utils.Helpers;
import org.codingmatters.ufc.ead.m1.nosql.twitter.bean.Tweet;
import org.elasticsearch.action.index.IndexResponse;

import static org.codingmatters.ufc.ead.m1.nosql.data.utils.Helpers.dateFromLocalDateTime;

public class CassandraTweetInjector implements TweetInjector {

    private final Session session;
    private PreparedStatement stmt;

    public CassandraTweetInjector(Session session) {
        this.session = session;
        this.stmt = this.session.prepare("" +
                "INSERT INTO ufcead.twitter_data(user_name, user_followersCount, text, lang, createdAt, mentions, htags, links) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
    }

    @Override
    public String inject(Tweet tweet) throws InjectorException {

            this.session.execute(new BoundStatement(this.stmt).bind(
                    tweet.getUser().getName(),
                    tweet.getUser().getFollowersCount(),
                    tweet.getText(),
                    tweet.getLang(),
                    tweet.getCreatedAt(),
                    tweet.getMentions(),
                    tweet.getHtags(),
                    tweet.getLinks()
            ));
        return "OK";
    }
}
