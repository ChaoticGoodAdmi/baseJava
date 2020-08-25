package com.urise.webapp.storage.serialization;

import com.urise.webapp.exceptions.StorageException;
import com.urise.webapp.model.*;

import java.io.*;
import java.time.LocalDate;
import java.util.*;

public class DataStreamSerialization implements SerializationStrategy {

    @Override
    public void doWrite(Resume resume, OutputStream os) throws IOException {
        try (DataOutputStream dos = new DataOutputStream(os)) {
            dos.writeUTF(resume.getUuid());
            dos.writeUTF(resume.getFullName());
            Map<ContactType, String> contacts = resume.getContacts();
            writeCollection(contacts.entrySet(), dos, entry -> {
                dos.writeUTF(entry.getKey().name());
                dos.writeUTF(entry.getValue());
            });
            Map<SectionType, Section> sections = resume.getSections();
            writeCollection(sections.entrySet(), dos, entry -> writeSection(dos, entry));
        }
    }

    @Override
    public Resume doRead(InputStream is) throws IOException {
        try (DataInputStream dis = new DataInputStream(is)) {
            String uuid = dis.readUTF();
            String fullName = dis.readUTF();
            Resume resume = new Resume(uuid, fullName);
            readItems(dis, () -> resume.setContact(ContactType.valueOf(dis.readUTF()), dis.readUTF()));
            readItems(dis, () -> {
                SectionType sectionType = SectionType.valueOf(dis.readUTF());
                resume.setSection(sectionType, readSection(dis, sectionType));
            });
            return resume;
        }
    }

    private interface ItemWriter<I> {
        void write(I i) throws IOException;
    }

    private interface ItemReader {
        void read() throws IOException;
    }

    public interface ItemLister<I> {
        I list() throws IOException;
    }

    private <I> void writeCollection(Collection<I> collection, DataOutputStream dos, ItemWriter<I> writer) throws IOException {
        dos.writeInt(collection.size());
        for (I item : collection) {
            writer.write(item);
        }
    }

    private void readItems(DataInputStream dis, ItemReader reader) throws IOException {
        int size = dis.readInt();
        for (int i = 0; i < size; i++) {
            reader.read();
        }
    }

    private <I> List<I> listItems(DataInputStream dis, ItemLister<I> lister) throws IOException {
        int size = dis.readInt();
        List<I> list = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            list.add(lister.list());
        }
        return list;
    }

    private void writeSection(DataOutputStream dos, Map.Entry<SectionType, Section> entry) throws IOException {
        SectionType sectionType = entry.getKey();
        Section section = entry.getValue();
        dos.writeUTF(sectionType.name());
        switch (sectionType) {
            case PERSONAL:
            case OBJECTIVE:
                dos.writeUTF(((TextSection) section).getText());
                break;
            case ACHIEVEMENT:
            case QUALIFICATIONS:
                writeCollection(((ListSection) section).getList(), dos, dos::writeUTF);
                break;
            case EDUCATION:
            case EXPERIENCE:
                writeCollection(((CompanySection) section).getList(), dos, company -> writeCompany(company, dos));
                break;
        }
    }

    private Section readSection(DataInputStream dis, SectionType sectionType) throws IOException {
        switch (sectionType) {
            case PERSONAL:
            case OBJECTIVE:
                return new TextSection(dis.readUTF());
            case ACHIEVEMENT:
            case QUALIFICATIONS:
                List<String> list = listItems(dis, dis::readUTF);
                return new ListSection(list);
            case EDUCATION:
            case EXPERIENCE:
                return new CompanySection(readCompanies(dis));
            default:
                throw new StorageException("No such ContactType");
        }
    }

    private List<Company> readCompanies(DataInputStream dis) throws IOException {
        return listItems(dis, () -> readCompany(dis));
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
        return listItems(dis, () -> readPosition(dis));
    }

    private void writePosition(Company.Position position, DataOutputStream dos) throws IOException {
        dos.writeUTF(position.getTitle());
        writeItemNullable(position.getDescription(), dos);
        writeLocalDate(position.getStartDate(), dos);
        writeLocalDate(position.getEndDate(), dos);
    }

    private Company.Position readPosition(DataInputStream dis) throws IOException {
        String title = dis.readUTF();
        String desc = readItemNullable(dis);
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
        writeItemNullable(link.getUrl(), dos);
        dos.writeUTF(link.getName());
    }

    private Link readLink(DataInputStream dis) throws IOException {
        return new Link(readItemNullable(dis), dis.readUTF());
    }

    private void writeItemNullable(String item, DataOutputStream dos) throws IOException {
        dos.writeUTF(Objects.requireNonNullElse(item, ""));
    }

    private String readItemNullable(DataInputStream dis) throws IOException {
        String line = dis.readUTF();
        if (line.equals("")) {
            return null;
        }
        return line;
    }
}
