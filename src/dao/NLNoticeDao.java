package dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import vo.Notice;

public class NLNoticeDao extends JdbcDaoSupport implements NoticeDao {

	private TransactionTemplate transactionTemplate;

	public void setTransactionTemplate(TransactionTemplate transactionTemplate) {
		this.transactionTemplate = transactionTemplate;
	}

	public int getCount(String field, String query) throws ClassNotFoundException, SQLException {
		String sql = "SELECT COUNT(*) CNT FROM NOTICES WHERE " + field + " LIKE ?";

		return getJdbcTemplate().queryForObject(sql, Integer.class, query);
	}

	public List<Notice> getNotices(int page, String field, String query) throws ClassNotFoundException, SQLException {
		int srow = 1 + (page - 1) * 15; // 1, 16, 31, 46, 61, ... an = a1 + (n-1)*d
		int erow = 15 + (page - 1) * 15; // 15, 30, 45, 60, 75, ...

		String sql = "SELECT * FROM";
		sql += "(SELECT ROWNUM NUM, N.* FROM (SELECT * FROM NOTICES WHERE " + field
				+ " LIKE ? ORDER BY \"REGDATE\" DESC) N)";
		sql += "WHERE NUM BETWEEN ? AND ?";

		return getJdbcTemplate().query(sql, new Object[] { "%" + query + "%", srow, erow },
				new BeanPropertyRowMapper<Notice>(Notice.class));
	}

	public int delete(String seq) throws ClassNotFoundException, SQLException {
		String sql = "DELETE NOTICES WHERE \"SEQ\"=?";
		return getJdbcTemplate().update(sql, seq);
	}

	public int update(Notice n) throws ClassNotFoundException, SQLException {
		String sql = "UPDATE NOTICES SET \"TITLE\"=?, \"CONTENT\"=?, \"FILESRC\"=? WHERE \"SEQ\"=?";
		return getJdbcTemplate().update(sql, n.getTitle(), n.getContent(), n.getFileSrc(), n.getSeq());
	}

	public Notice getNotice(String seq) throws ClassNotFoundException, SQLException {
		String sql = "SELECT * FROM NOTICES WHERE \"SEQ\"= ?";
		return getJdbcTemplate().queryForObject(sql, new Object[] { seq }, new RowMapper<Notice>() {

			@Override
			public Notice mapRow(ResultSet rs, int rowNum) throws SQLException {
				Notice n = new Notice();
				n.setSeq(rs.getString("seq"));
				n.setTitle(rs.getString("title"));
				n.setWriter(rs.getString("writer"));
				n.setRegdate(rs.getDate("regdate"));
				n.setHit(rs.getInt("hit"));
				n.setContent(rs.getString("content"));
				n.setFileSrc(rs.getString("fileSrc"));
				return n;
			}
		});
	}

	public int insert(Notice n) throws ClassNotFoundException, SQLException {
		String sql = "INSERT INTO NOTICES(\"SEQ\", \"TITLE\", \"CONTENT\", \"WRITER\", \"REGDATE\", \"HIT\", \"FILESRC\") "
				+ "VALUES( (SELECT MAX(TO_NUMBER(\"SEQ\"))+1 FROM NOTICES), :title, 'newlec', :content, SYSDATE, 0, ?)";
		return getJdbcTemplate().update(sql, n.getTitle(), n.getContent(), n.getFileSrc());
	}

	public void insertAndPointUpOfMember(final Notice n, final String uid) {
		String sql = "INSERT INTO NOTICES(\"SEQ\", \"TITLE\", \"CONTENT\", \"WRITER\", \"REGDATE\", \"HIT\", \"FILESRC\") "
				+ "VALUES( (SELECT MAX(TO_NUMBER(\"SEQ\"))+1 FROM NOTICES), ?, 'newlec', ?, SYSDATE, 0, ?)";
		String sqlPoint = "UPDATE \"MEMBER\" SET POINT = POINT + 1 WHERE \"UID\" = ?";

		transactionTemplate.execute(new TransactionCallbackWithoutResult() {

			@Override
			protected void doInTransactionWithoutResult(TransactionStatus arg0) {
				getJdbcTemplate().update(sql, n.getTitle(), n.getContent(), n.getFileSrc());
				getJdbcTemplate().update(sqlPoint, uid);
			}
		});
	}
}
