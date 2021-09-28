package com.vizor.mobile;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vizor.mobile.aggregator.TweetAggregator;
import com.vizor.mobile.aggregator.TweetAggregatorFactory;
import com.vizor.mobile.twitter.Rule;
import com.vizor.mobile.twitter.TwitterStreamConnector;
import com.vizor.mobile.twitter.TwitterStreamConnectorFactory;
import io.prometheus.client.exporter.HTTPServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Сервис по обработке потока твитов, сформированного по заранее определенным правилам.
 *
 * На старте приложение загружает конфигурацию потока твитов (https://developer.twitter
 * .com/en/docs/twitter-api/tweets/filtered-stream/integrate/build-a-rule#build). Конфигурация представляет собой набор
 * пар значений (value=%value%, tag=%tag%). Далее создается экземпляр {@link TwitterStreamConnector} с помощью которого
 * мы настраиваем поток через специальный API и подписываемся на его обновления. Каждый полученный твит
 * обрабатывается в {@link com.vizor.mobile.aggregator.PrometheusTweetAggregator}, для того чтобы экспортировать
 * интересующие нас метрики.
 *
 * Актуальные результаты по обработке твитов запущенного локально приложения можно получить по URL:
 * http//localhost:8080/metrics. 
 */
public class Main
{
    private static final Logger LOG = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws IOException, InterruptedException
    {
        // Читаем данные для авторизации в Twitter API. Задавать их можно через Java Options запуская приложение
        // следующим образом:
        //   _JAVA_OPTIONS="-Dapi.key=... -Dsecret.key=..." ./gradlew run
        // Аналогично эти же опции можно настроить в IDE
        String apiKey = checkNotNull(System.getProperty("api.key"), "Property api.key is missing");
        String secretKey = checkNotNull(System.getProperty("secret.key"), "Property secret.key is missing");

        // Создаем и настраиваем TwitterStreamConnector
        TwitterStreamConnector connector = new TwitterStreamConnectorFactory().createConnector(apiKey, secretKey);
        List<Rule> rules = new ArrayList<>();
        try(InputStream rulesStream = Main.class.getResourceAsStream("/rules.json"))
        {
            Type type = new TypeToken<List<ConfigRule>>(){}.getType();
            InputStreamReader reader = new InputStreamReader(checkNotNull(rulesStream, "Can't find /rules.json"));
            rules = new Gson().fromJson(reader, type);
        }
        catch (IOException e)
        {
            LOG.error(e.getMessage(), e);
            System.exit(2);
        }
        // Создаем HTTPServer, который будет выставлять посчитанные нами метрики. Посмотреть их можно с помощью команды
        //  curl localhost:8080/metrics
        // Либо открыть этот адрес в браузере.
        // HTTPServer запускается в daemon режиме, это значит, что он не будет блокировать текущий поток.
        new HTTPServer(8080, true);

        // Создаем обработчик твитов, в нем будет происходить подсчет всех метрик, требуемых в задании.
        TweetAggregator aggregator = new TweetAggregatorFactory().createPrometheusAggregator();
        connector.listenStream(rules, aggregator::aggregateTweet);
    }

    public static <T> T checkNotNull(T value, String message)
    {
        if (value == null)
        {
            throw new IllegalArgumentException(message);
        }

        return value;
    }
}
