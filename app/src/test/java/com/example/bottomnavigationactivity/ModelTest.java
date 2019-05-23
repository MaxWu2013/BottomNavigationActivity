package com.example.bottomnavigationactivity;

import com.example.bottomnavigationactivity.model.Field;
import com.example.bottomnavigationactivity.model.Links;
import com.example.bottomnavigationactivity.model.Record;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ModelTest {

    @Test
    public void test_model_Field(){
        Field field = new Field();
        final String ID = "id";
        final String TYPE = "type";

        field.setId(ID);
        field.setType(TYPE);

        assertEquals(ID,field.getId());
        assertEquals(TYPE,field.getType());
    }

    @Test
    public void test_model_Links(){
        final String NEXT = "NEXT";
        final String START = "START";

        Links links = new Links();
        links.setNext(NEXT);
        links.setStart(START);

        assertEquals(NEXT,links.getNext());
        assertEquals(START,links.getStart());
    }

    @Test
    public void test_mode_Record(){
        final int ID = 99;
        final String VOLUME = "99999";
        final String QUARTER = "2008-Q3";

        Record record = new Record();
        record.setVolume_of_sms(VOLUME);
        record.set_id(ID);
        record.setQuarter(QUARTER);

        assertEquals(ID,record.get_id());
        assertEquals(VOLUME,record.getVolume_of_sms());
        assertEquals(QUARTER,record.getQuarter());
    }



}
