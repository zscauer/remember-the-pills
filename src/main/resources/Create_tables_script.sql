DROP DATABASE IF EXISTS remember_the_pills;
CREATE DATABASE remember_the_pills;

DROP TABLE IF EXISTS bot_users;
CREATE TABLE bot_users (
    id BIGINT NOT NULL,
    locale varchar(5),
    bot_state varchar(30),
    CONSTRAINT PK_bot_users_id PRIMARY KEY(id)
);

DROP TABLE IF EXISTS reminds;
CREATE TABLE reminds (
    id BIGSERIAL,
    bot_user_id BIGINT NOT NULL,
    remind_name varchar(100) NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    CONSTRAINT PK_reminds_id PRIMARY KEY(id),
    CONSTRAINT FK_bot_user_id FOREIGN KEY(bot_user_id) REFERENCES bot_users(id)
);

DROP TABLE IF EXISTS routine_notifications;
CREATE TABLE routine_notifications (
    id BIGSERIAL,
    remind_id BIGINT NOT NULL,
    notification_time time NOT NULL,
    CONSTRAINT PK_routine_reminds_id PRIMARY KEY(id),
    CONSTRAINT FK_remind_id FOREIGN KEY(remind_id) REFERENCES reminds(id)
);