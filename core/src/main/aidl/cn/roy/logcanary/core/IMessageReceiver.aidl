package cn.roy.logcanary.core;

interface IMessageReceiver {
    oneway void receive(String message);
}