package dao;

import java.sql.SQLException;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import vo.Member;

public class NLMemberDao extends JdbcDaoSupport implements MemberDao {
	public Member getMember(String uid) throws ClassNotFoundException, SQLException {
		String sql = "SELECT * FROM MEMBER WHERE \"UID\"=?";

		return getJdbcTemplate().queryForObject(sql, new BeanPropertyRowMapper<Member>(Member.class), uid);
	}

	public int insert(Member member) throws ClassNotFoundException, SQLException {
		String sql = "INSERT INTO MEMBER(\"UID\", \"PWD\", \"NAME\", GENDER, BIRTH, IS_LUNAR, CPHONE, EMAIL, HABIT, REGDATE) "
				+ "VALUES( ?, ?, ?, ?, ?, ?, ?, ?, ?, SYSDATE)";
		return getJdbcTemplate().update(sql, member.getUid(), member.getPwd(), member.getName(), member.getGender(),
				member.getBirth(), member.getIsLunar(), member.getCPhone(), member.getEmail(), member.getHabit());
	}
}
