package guru.qa.niffler.db.sjdbs;

import guru.qa.niffler.db.model.Authority;
import guru.qa.niffler.db.model.AuthorityEntity;
import guru.qa.niffler.db.model.CurrencyValues;
import guru.qa.niffler.db.model.UserAuthEntity;
import guru.qa.niffler.db.model.UserEntity;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class UserAuthEntityRowMapper implements RowMapper<UserAuthEntity> {

    public static UserAuthEntityRowMapper INSTANCE = new UserAuthEntityRowMapper();

    @Override
    public UserAuthEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        UserAuthEntity user = new UserAuthEntity();
        user.setId(rs.getObject(1, UUID.class));
        user.setUsername(rs.getString(2));
        user.setPassword(rs.getString(3));
        user.setEnabled(rs.getBoolean(4));
        user.setAccountNonExpired(rs.getBoolean(5));
        user.setAccountNonLocked(rs.getBoolean(6));


        AuthorityEntity authority = new AuthorityEntity();
        authority.setId(rs.getObject(8, UUID.class));
        authority.setAuthority(Authority.valueOf(rs.getString(10)));
        user.getAuthorities().add(authority);
        return user;
    }


}
