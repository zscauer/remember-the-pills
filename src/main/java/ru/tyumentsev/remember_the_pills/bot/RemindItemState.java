package ru.tyumentsev.remember_the_pills.bot;

public enum RemindItemState implements States{
    NEW_ITEM,
    ITEM_EDIT,
    ITEM_DELETE,
    ITEM_SET_NAME,
    ITEM_SET_START_DATE,
    ITEM_SET_END_DATE,
    ITEM_SUCCESSFULLY_ADDED,
    NOTIFICATION_ADD_TIME,
    NOTIFICATION_SUCCESSFULLY_ADDED,
    ERROR_INCORRECT_DATE_FORMAT;
}
