package com.codenjoy.dojo.services.controller.chat;

import com.codenjoy.dojo.services.chat.ChatControl;
import com.codenjoy.dojo.services.chat.ChatType;
import com.codenjoy.dojo.services.chat.Filter;
import com.codenjoy.dojo.services.dao.Chat;
import com.codenjoy.dojo.web.rest.pojo.PMessage;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class ChatCommandTest {

    private ChatControl control;
    private ChatCommand command;

    @Before
    public void setup() {
        // given
        control = mock(ChatControl.class);
        command = new ChatCommand(control);
    }

    @After
    public void after() {
        verifyNoMoreInteractions(control);
    }

    @Test
    public void testGet() {
        // given
        when(control.get(anyInt(), anyString()))
                .thenReturn(someMessage(1));

        // when
        String result = command.process(new ChatRequest("{'command':'get', " +
                "'data':{'id':1, 'room':'room'}}"));

        // then
        verify(control).get(1, "room");

        assertOne(result);
    }

    private void assertOne(String result) {
        assertEquals("{'id':0,'text':'message1','room':'room1','topicId':13," +
                "'playerId':'player1','playerName':'playerName1','time':12346}",
                fix(result));
    }

    private PMessage someMessage(int id) {
        return PMessage.from(new Chat.Message(
                "room" + id, 12 + id, ChatType.TOPIC,
                "player" + id, 12345L + id, "message" + id), "playerName" + id);
    }

    @Test
    public void testDelete() {
        // given
        when(control.delete(anyInt(), anyString()))
                .thenReturn(true);

        // when
        String result = command.process(new ChatRequest("{'command':'delete', " +
                "'data':{'id':1, 'room':'room'}}"));

        // then
        verify(control).delete(1, "room");
        assertEquals("true", result);
    }

    @Test
    public void testAllInRoom() {
        // given
        when(control.getAllRoom(any(Filter.class)))
                .thenReturn(Arrays.asList(someMessage(1), someMessage(2)));

        // when
        String result = command.process(new ChatRequest("{'command':'getAllRoom', " +
                "'data':{'room':'otherRoom', 'count':1, " +
                "'afterId':2, 'beforeId':5, 'inclusive':true}}"));

        // then
        ArgumentCaptor<Filter> captor = ArgumentCaptor.forClass(Filter.class);
        verify(control).getAllRoom(captor.capture());
        assertEquals("Filter(room=otherRoom, count=1, afterId=2, " +
                        "beforeId=5, inclusive=true)",
                captor.getValue().toString());

        assertList(result);
    }

    private void assertList(String result) {
        assertEquals("[{'id':0,'text':'message1','room':'room1','topicId':13," +
                "'playerId':'player1','playerName':'playerName1','time':12346}," +
                "{'id':0,'text':'message2','room':'room2','topicId':14," +
                "'playerId':'player2','playerName':'playerName2','time':12347}]",
                fix(result));
    }

    private String fix(String string) {
        return string.replace("\"", "'");
    }

    @Test
    public void testAllInField() {
        // given
        when(control.getAllField(any(Filter.class)))
                .thenReturn(Arrays.asList(someMessage(1), someMessage(2)));

        // when
        String result = command.process(new ChatRequest("{'command':'getAllField', " +
                "'data':{'room':'room', 'count':10, " +
                "'afterId':3, 'beforeId':4, 'inclusive':false}}"));

        // then
        ArgumentCaptor<Filter> captor = ArgumentCaptor.forClass(Filter.class);
        verify(control).getAllField(captor.capture());
        assertEquals("Filter(room=room, count=10, afterId=3, " +
                        "beforeId=4, inclusive=false)",
                captor.getValue().toString());

        assertList(result);
    }

    @Test
    public void testAllInTopic() {
        // given
        when(control.getAllTopic(anyInt(), any(Filter.class)))
                .thenReturn(Arrays.asList(someMessage(1), someMessage(2)));

        // when
        String result = command.process(new ChatRequest("{'command':'getAllTopic', " +
                "'data':{'id':12, 'room':'room2', 'count':3, " +
                "'afterId':4, 'beforeId':6, 'inclusive':true}}"));

        // then
        ArgumentCaptor<Filter> captor = ArgumentCaptor.forClass(Filter.class);
        verify(control).getAllTopic(eq(12), captor.capture());
        assertEquals("Filter(room=room2, count=3, afterId=4, " +
                        "beforeId=6, inclusive=true)",
                captor.getValue().toString());

        assertList(result);
    }

    @Test
    public void testPostRoom() {
        // given
        when(control.postRoom(anyString(), anyString()))
                .thenReturn(someMessage(1));

        // when
        String result = command.process(new ChatRequest("{'command':'postRoom', " +
                "'data':{'text':'message4', 'room':'otherRoom'}}"));

        // then
        verify(control).postRoom("message4", "otherRoom");

        assertOne(result);
    }

    @Test
    public void testPostField() {
        // given
        when(control.postField(anyString(), anyString()))
                .thenReturn(someMessage(1));

        // when
        String result = command.process(new ChatRequest("{'command':'postField', " +
                "'data':{'text':'message2', 'room':'room3'}}"));

        // then
        verify(control).postField("message2", "room3");

        assertOne(result);
    }

    @Test
    public void testPostTopic() {
        // given
        when(control.postTopic(anyInt(), anyString(), anyString()))
                .thenReturn(someMessage(1));

        // when
        String result = command.process(new ChatRequest("{'command':'postTopic', " +
                "'data':{'id': 12, 'text':'message1', 'room':'room'}}"));

        // then
        verify(control).postTopic(12, "message1", "room");

        assertOne(result);
    }

    @Test
    public void testPostTopic_messageIsNull() {
        // when
        String result = command.process(new ChatRequest("{'command':'postTopic', " +
                "'data':{'id': 12, 'text':null, 'room':'room'}}"));

        // then
        verify(control, never()).postTopic(anyInt(), anyString(), anyString());

        assertEquals("{'error':'JSONException'," +
                "'message':'JSONObject[\\'text\\'] not a string.'}",
                fix(result));
    }

    @Test
    public void testPostTopic_messageIsNotExists() {
        // when
        String result = command.process(new ChatRequest("{'command':'postTopic', " +
                "'data':{'id': 12, 'room':'room'}}"));

        // then
        verify(control, never()).postTopic(anyInt(), anyString(), anyString());

        assertEquals("{'error':'JSONException'," +
                        "'message':'JSONObject[\\'text\\'] not found.'}",
                fix(result));
    }
}