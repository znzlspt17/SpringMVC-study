package dao;

import java.sql.SQLException;
import java.util.List;

import vo.Notice;

public interface NoticeDao {

	public int getCount(String field, String query) throws ClassNotFoundException, SQLException;

	public int delete(String seq) throws ClassNotFoundException, SQLException;

	public int update(Notice n) throws ClassNotFoundException, SQLException;

	public int insert(Notice n) throws ClassNotFoundException, SQLException;

	public List<Notice> getNotices(int page, String field, String query) throws ClassNotFoundException, SQLException;

	public Notice getNotice(String seq) throws ClassNotFoundException, SQLException;

	public void insertAndPointUpOfMember(Notice n, String uid) throws ClassNotFoundException, SQLException;
}
