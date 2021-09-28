# Запуск приложения

   1. Для запуска приложения следует добавить свой api.key и secret.key
в файл build.gradle в поле applicationDefaultJvmArgs.
   2. Далее комадой ./gradlew build в терминале следует собрать проект.
   3. ./gradlew run запуск проекта
   4. По адресу http://localhost:8080/metrics просмотр метрик.

   5. Так же можно запустить приложение при помощи команды
      _JAVA_OPTIONS="-Dapi.key=... -Dsecret.key=..." ./gradlew run
      указав свой api и secret key.
  