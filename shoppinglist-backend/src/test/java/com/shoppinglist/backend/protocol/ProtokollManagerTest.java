package com.shoppinglist.backend.protocol;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ProtokollManagerTest {

    @TempDir
    Path tempDir;

    @Test
    public void testWriteAndRead() throws IOException {
        Path filePath = tempDir.resolve("protokoll.log");
        ProtokollManager manager = new ProtokollManager();

        ProtokollObjekt obj1 = new ProtokollObjekt("key1", "typ1", "file1");
        ProtokollObjekt obj2 = new ProtokollObjekt("key2", "typ2", "file2");
        ProtokollObjekt obj3 = new ProtokollObjekt("key1", "typ3", "file3");

        manager.write(obj1, filePath);
        manager.write(obj2, filePath);
        manager.write(obj3, filePath);

        List<ProtokollObjekt> all = manager.readAll(filePath);
        assertEquals(3, all.size());
        assertEquals("key1", all.get(0).getKey());
        assertEquals("key2", all.get(1).getKey());
        assertEquals("key1", all.get(2).getKey());

        List<ProtokollObjekt> byKey1 = manager.findByKey("key1", filePath);
        assertEquals(2, byKey1.size());
        assertEquals("typ1", byKey1.get(0).getTyp());
        assertEquals("typ3", byKey1.get(1).getTyp());

        List<ProtokollObjekt> byKeyNone = manager.findByKey("nonexistent", filePath);
        assertTrue(byKeyNone.isEmpty());
    }
}
