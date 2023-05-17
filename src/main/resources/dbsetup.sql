CREATE TABLE IF NOT EXISTS guild_data
(
    guildId CHAR(18) NOT NULL,
    welcome_channel CHAR(18),
    ideas_channel CHAR(18),
    primary key (guildId)
);