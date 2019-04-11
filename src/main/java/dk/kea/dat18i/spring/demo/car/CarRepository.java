package dk.kea.dat18i.spring.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Repository
public class CarRepository {

    @Autowired
    private JdbcTemplate jdbc;


    /* querying examples: https://www.mkyong.com/spring/spring-jdbctemplate-querying-examples/ */

    public Car findCar(int id) {
        String sql = "SELECT * FROM cars WHERE id= ?";
        Car car = jdbc.queryForObject(sql, new Object[] {id}, new BeanPropertyRowMapper<>(Car.class));

        return car;
    }

    public List<Car> findAllCars() {
        String sql = "SELECT * FROM cars";
        List<Car> cars = jdbc.query(sql, new BeanPropertyRowMapper(Car.class));
        return cars;
    }

    public void addCar(Car car) {
        String sql = "INSERT INTO cars (reg, brand, color, max_speed) VALUES(?, ?, ?, ?)";
//        jdbc.update(sql, car.getReg(), car.getBrand(), car.getColor(), car.getMaxSpeed());

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                // tell preparedStatement after "," what column to get back => id
                PreparedStatement ps = connection.prepareStatement(sql, new String[] {"id"});
                ps.setString(1, car.getReg());
                ps.setString(2, car.getBrand());
                ps.setString(3, car.getColor());
                ps.setDouble(4, car.getMaxSpeed());
                return ps;
            }
        }, keyHolder);

        System.out.println(keyHolder.getKey().intValue());
    }


    public void deleteCar(int id) {
        String sql = "DELETE FROM cars WHERE id = ?";
        jdbc.update(sql, id);
    }

    public void editCar(int id, Car car) {
        String sql = "UPDATE cars SET reg = ?, brand = ?, color = ?, max_speed = ? WHERE id = ?";
        jdbc.update(sql, car.getReg(), car.getBrand(), car.getColor(), car.getMaxSpeed(), id);
    }
}
