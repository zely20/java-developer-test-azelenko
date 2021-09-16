package com.vizor.mobile.twitter;

import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

/**
 * Интерфейс описывающий протокол взаимодействия с TwitterStreamApi со стороны приложения.
 *
 * В рамках этого интерфейса нам "интересна" только настройка правил и подключение к потоку событий.
 */
public interface TwitterStreamConnector
{
    /**
     * Подключиться к потоку твитов. В переданный {@link Consumer} будут передаваться полученные твиты по мере
     * поступления данных.
     *
     * Перед тем как подключиться к потоку, метод настраивает конфигурацию потока твитов, применяя переданный список
     * правил. Если поток уже настроен с использованием других правил, например другим клиентом или старой версией
     * конфигурации, то неактуальные правила нужно убрать из конфигурации. То есть к моменту подключения к потоку,
     * конфигурация должна быть актуальна переданному списку правил.
     *
     * Метод гарантирует автоматическую обработку переподключений и обрывов связи согласно документации. Подробнее
     * смотри по приведенной ссылке.
     * {@see https://developer.twitter.com/en/docs/twitter-api/tweets/filtered-stream/integrate/handling-disconnections}
     */
    void listenStream(List<Rule> ruleList, Consumer<Tweet> streamConsumer) throws IOException, InterruptedException;
}
