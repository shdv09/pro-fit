# pro-fit
fitness club management system - система автоматизации работы фитнес-клуба

web-application - основная часть бэкенда веб-приложения, http port - 8000;
auth-service - сервис авторизации, генерирует JWT-токен. Тип авторизации в сервисе - basic, http port - 8004. 
client-service - сервис информации о клиентах клуба. Тип авторизации в сервисе - bearer, http port - 8001.
visit-service - сервис учёта посещений. Тип авторизации в сервисе - bearer, http port - 8002.
appointment-service - сервис записи на первональную тренировку. Тип авторизации в сервисе - bearer, http port - 8003.
report-service - сервис отчётов. Тип авторизации в сервисе - bearer, http port - 8006.
