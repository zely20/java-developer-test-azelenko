package com.vizor.mobile.twitter;

import java.util.List;

/**
 * Предоставляет информацию о полученном твите.  * Набор методов урезан до необходимого мимимума в рамках поставленной
 * задачи.
 */
public interface Tweet
{
    /**
     * Текст твита
     */
    String getText();

    /**
     * Список правил, под которые подпадает этот твит.
     */
    List<Rule> getMatchingRules();

    void setMatchingRules(List<Rule> matchingRules);
}
