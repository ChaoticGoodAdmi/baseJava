package com.urise.webapp.storage.serialization;

import com.urise.webapp.model.*;

import java.io.*;
import java.util.*;

public class DataStreamSerialization implements SerializationStrategy {

    @Override
    public void doWrite(Resume resume, OutputStream os) throws IOException {
        try (DataOutputStream dos = new DataOutputStream(os)) {
            dos.writeUTF(resume.getUuid());
            dos.writeUTF(resume.getFullName());
            Map<ContactType, String> contacts = resume.getContacts();
            writeMap(contacts, dos);
            Map<SectionType, Section> sections = resume.getSections();
            writeSections(sections, dos);
        }
    }

    @Override
    public Resume doRead(InputStream is) throws IOException {
        try (DataInputStream dis = new DataInputStream(is)) {
            String uuid = dis.readUTF();
            String fullName = dis.readUTF();
            Resume resume = new Resume(uuid, fullName);
            Map<ContactType, String> contacts = readMap(dis);
            resume.setContacts(contacts);
            Map<SectionType, Section> sections = readSections(dis);
            resume.setSections(sections);
            return resume;
        }
    }

    private <K, V> void writeMap(Map<K, V> map, DataOutputStream dos) throws IOException {
        dos.writeInt(map.size());
        for (Map.Entry<K, V> entry : map.entrySet()) {
            dos.writeUTF(entry.getKey().toString());
            dos.writeUTF(entry.getValue().toString());
        }
    }

    private <K, V> Map<K, V> readMap(DataInputStream dis) throws IOException {
        int size = dis.readInt();
        Map<K, V> map = new HashMap<>();
        for (int i = 0; i < size; i++) {
            map.put((K) dis.readUTF(), (V) dis.readUTF());
        }
        return map;
    }

    private void writeSections(Map<SectionType, Section> sections, DataOutputStream dos) throws IOException {
        dos.writeInt(sections.size());
        for(Map.Entry<SectionType, Section> entry : sections.entrySet()) {
            writeSection(entry.getKey(), entry.getValue(), dos);
        }
    }

    private  Map<SectionType, Section> readSections(DataInputStream dis) throws IOException {
        Map<SectionType, Section> sections = new EnumMap<>(SectionType.class);
        int size = dis.readInt();
        for (int i = 0; i < size; i++) {
            String sectionType = dis.readUTF();
            switch (sectionType) {
                case "PERSONAL":
                case "OBJECTIVE":
                    sections.put(SectionType.valueOf(sectionType), new TextSection(dis.readUTF()));
                    break;
                case "ACHIEVEMENT":
                case "QUALIFICATIONS":
                    List<String> list = readList(dis);
                    sections.put(SectionType.valueOf(sectionType), new ListSection(list));
                    break;
                case "EDUCATION":
                case "EXPERIENCE":
                    List<Company> companies = readList(dis);
                    sections.put(SectionType.valueOf(sectionType), new CompanySection(companies));
                    break;
            }
        }
        return sections;
    }

    private void writeSection(SectionType sectionType, Section section, DataOutputStream dos) throws IOException {
        dos.writeUTF(sectionType.name());
        switch (sectionType) {
            case PERSONAL:
            case OBJECTIVE:
                dos.writeUTF(section.toString());
                break;
            case ACHIEVEMENT:
            case QUALIFICATIONS:
                ListSection listSection = (ListSection) section;
                writeList(listSection.getList(), dos);
                break;
            case EDUCATION:
            case EXPERIENCE:
                CompanySection companySection = (CompanySection) section;
                writeList(companySection.getList(), dos);
                break;
        }
    }

    private <E> void writeList(List<E> list, DataOutputStream dos) throws IOException {
        dos.writeInt(list.size());
        for (E element : list) {
            dos.writeUTF(element.toString());
        }
    }

    private <E> List<E> readList(DataInputStream dis) throws IOException {
        int size = dis.readInt();
        List<E> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            list.add((E) dis.readUTF());
        }
        return list;
    }
}
