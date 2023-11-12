CREATE TABLE IF NOT EXISTS guild_data
(
    guildId NOT NULL,
    welcome_channel CHAR,
    ideas_channel CHAR,
    ticket_role CHAR,
    auto_role CHAR,
    primary key (guildId)
);
