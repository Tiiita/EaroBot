CREATE TABLE IF NOT EXISTS guild_data
(
    guildId NOT NULL,
    welcome_channel CHAR,
    ideas_channel CHAR,
    ticket_role CHAR,
    primary key (guildId)
);

CREATE TABLE IF NOT EXISTS guild_data
(
    guildId NOT NULL,
    userId CHAR,
    userLevel INT DEFAULT 0,
    ticket_role CHAR,
    primary key (guildId)
);