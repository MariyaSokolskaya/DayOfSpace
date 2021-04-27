package com.example.dayofspace;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Text;

@Root(name = "speak")
class VoiceChoice{
    @Attribute
    String version = "1.0";

    @Attribute
    @Namespace(prefix = "xml")
    String lang;

    @Element
    Voice voice;
}

class Voice{
    @Attribute
    @Namespace(prefix = "xml")
    String lang;

    @Attribute
    @Namespace(prefix = "xml")
    String gender;

    @Attribute
    String name;

    @Text
    String text;
}

//<speak version='1.0' xml:lang='<LANG>'>
// <voice xml:lang='<LANG>' xml:gender='<GENDER>' name='<NAME>'>
// I've Just seen A face
// </voice>
// </speak>