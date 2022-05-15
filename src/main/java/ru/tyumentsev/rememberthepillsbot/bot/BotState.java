package ru.tyumentsev.rememberthepillsbot.bot;
/**
 * Represents current state of user's dialog with bot,
 * that used to define type of to user's query.
 * 
 * @see States
 */
public enum BotState implements States {
    SHOW_MAIN_MENU,
    SHOW_HELP_MENU,
    SHOW_USER_INFO,
    ITEM_ADD,
    ITEM_CHOOSE,
    ITEM_EDIT,
    ITEM_DELETE,
    ITEM_DELETE_ALL,
    ITEM_ADD_NOTIFICATION,
    ITEM_EDIT_NOTIFICATION,
    ITEM_DELETE_NOTIFICATION,
    ITEM_DELETE_ALL_NOTIFICATIONS,
    ADD_START_DATE,
    ADD_END_DATE;
}
