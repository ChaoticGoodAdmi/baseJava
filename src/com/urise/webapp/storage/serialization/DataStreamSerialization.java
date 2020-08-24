package com.urise.webapp.storage.serialization;

import com.urise.webapp.model.*;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

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

    private Map<ContactType, String> readMap(DataInputStream dis) throws IOException {
        int size = dis.readInt();
        Map<ContactType, String> map = new EnumMap<>(ContactType.class);
        for (int i = 0; i < size; i++) {
            map.put(ContactType.valueOf(dis.readUTF()), dis.readUTF());
        }
        return map;
    }

    private void writeSections(Map<SectionType, Section> sections, DataOutputStream dos) throws IOException {
        dos.writeInt(sections.size());
        for (Map.Entry<SectionType, Section> entry : sections.entrySet()) {
            writeSection(entry.getKey(), entry.getValue(), dos);
        }
    }

    private Map<SectionType, Section> readSections(DataInputStream dis) throws IOException {
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
                    List<Company> companies = readCompanies(dis);
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
                writeCompanies(companySection.getList(), dos);
                break;
        }
    }

    private <E> void writeList(List<E> list, DataOutputStream dos) throws IOException {
        dos.writeInt(list.size());
        for (E element : list) {
            dos.writeUTF(element.toString());
        }
    }

    private List<String> readList(DataInputStream dis) throws IOException {
        int size = dis.readInt();
        List<String> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            list.add(dis.readUTF());
        }
        return list;
    }

    private void writeCompanies(List<Company> companies, DataOutputStream dos) throws IOException {
        dos.writeInt(companies.size());
        for (Company company : companies) {
            writeCompany(company, dos);
        }
    }

    private List<Company> readCompanies(DataInputStream dis) throws IOException {
        List<Company> companies = new ArrayList<>();
        int size = dis.readInt();
        for (int i = 0; i < size; i++) {
            companies.add(readCompany(dis));
        }
        return companies;
    }

    private void writeCompany(Company company, DataOutputStream dos) throws IOException {
        writeLink(company.getHomePage(), dos);
        writePositions(company.getPositions(), dos);
    }

    private Company readCompany(DataInputStream dis) throws IOException {
        return new Company(readLink(dis), readPositions(dis));
    }

    private void writePositions(List<Company.Position> positions, DataOutputStream dos) throws IOException {
        dos.writeInt(positions.size());
        for (Company.Position position : positions) {
            writePosition(position, dos);
        }
    }

    private List<Company.Position> readPositions(DataInputStream dis) throws IOException {
        List<Company.Position> positions = new ArrayList<>();
        int size = dis.readInt();
        for (int i = 0; i < size; i++) {
            positions.add(readPosition(dis));
        }
        return positions;
    }

    private void writePosition(Company.Position position, DataOutputStream dos) throws IOException {
        dos.writeUTF(position.getTitle());
        dos.writeUTF(position.getDescription());
        writeLocalDate(position.getStartDate(), dos);
        writeLocalDate(position.getEndDate(), dos);
    }

    private Company.Position readPosition(DataInputStream dis) throws IOException {
        String title = dis.readUTF();
        String desc = dis.readUTF();
        LocalDate startDate = readLocalDate(dis);
        LocalDate endDate = readLocalDate(dis);
        return new Company.Position(title, desc,
                startDate.getYear(), startDate.getMonth(),
                endDate.getYear(), endDate.getMonth());
    }

    private void writeLocalDate(LocalDate date, DataOutputStream dos) throws IOException {
        dos.writeInt(date.getYear());
        dos.writeInt(date.getMonth().getValue());
    }

    private LocalDate readLocalDate(DataInputStream dis) throws IOException {
        return LocalDate.of(dis.readInt(), dis.readInt(), 1);
    }

    private void writeLink(Link link, DataOutputStream dos) throws IOException {
        dos.writeUTF(link.getUrl());
        dos.writeUTF(link.getName());
    }

    private Link readLink(DataInputStream dis) throws IOException {
        return new Link(dis.readUTF(), dis.readUTF());
    }

}
