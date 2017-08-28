package com.devianta.dbworker;

import java.io.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

public class DBWorker {

    @SuppressWarnings("unchecked")
    public static synchronized <T extends Serializable> T readSerialDB(String dbPath, String fileName, Class<T> cls) {
        File dataFile = getFile(dbPath, fileName);
        T db = null;

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(dataFile))) {
            db = (T) ois.readObject();
        } catch (EOFException e) {
            db = null;
        } catch (ClassNotFoundException | IOException e) {
            throw new IllegalArgumentException("Incorrect data file " + dbPath);
        }

        try {
            if (db == null) {
                db = cls.newInstance();
            }
        } catch (InstantiationException | IllegalAccessException e) {
            throw new IllegalArgumentException("Creation instanse of object " + cls + " exception");
        }

        return db;
    }

    public static synchronized <T extends Serializable> void writeSerialDB(String dbPath, String fileName, T object) {
        File dataFile = getFile(dbPath, fileName);

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(dataFile))) {
            oos.writeObject(object);
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Write to file " + dbPath + " Exception");
        }
    }

    @SuppressWarnings("unchecked")
    public static synchronized <T> T readXMLDB(String dbPath, String fileName, Class<T> cls) {
        File dataFile = getFile(dbPath, fileName);
        T object = null;
        try {
            JAXBContext jaxbC = JAXBContext.newInstance(cls);
            Unmarshaller unmarshaller = jaxbC.createUnmarshaller();
            object = (T) unmarshaller.unmarshal(dataFile);
        } catch (JAXBException e) {
            throw new IllegalArgumentException("Can't read data from file " + dbPath);
        }

        return object;
    }

    public static synchronized <T> void writeXMLDB(String dbPath, String fileName, T object) {
        File dataFile = getFile(dbPath, fileName);
        try {
            JAXBContext jaxbC = JAXBContext.newInstance(object.getClass());
            Marshaller marSh = jaxbC.createMarshaller();
            marSh.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marSh.marshal(object, dataFile);
        } catch (JAXBException e) {
            throw new IllegalArgumentException("Can't save data to file " + dbPath);
        }
    }

    public static String[] listFiles(String dbPath, String extension) {
        File dataPath = getDir(dbPath);
        String[] list = dataPath.list((d, n) -> n.toLowerCase().endsWith("." + extension));
        return list;
    }

    private static File getFile(String dbPath, String fileName) {
        File dataFile = new File(dbPath, fileName);
        if (!dataFile.exists()) {
            try {
                dataFile.createNewFile();
            } catch (IOException e) {
                throw new IllegalArgumentException("Can not access to create file " + dbPath);
            }
        }
        if (dataFile.isDirectory()) {
            throw new IllegalArgumentException("File expected directory found " + dbPath);
        }
        return dataFile;
    }

    private static File getDir(String dbPath) {
        File dataPath = new File(dbPath);
        if (!dataPath.exists()) {
            throw new IllegalArgumentException("Not found path: " + dbPath);
        }
        if (dataPath.isFile()) {
            throw new IllegalArgumentException("Directory expected file found " + dbPath);
        }
        return dataPath;
    }

}
