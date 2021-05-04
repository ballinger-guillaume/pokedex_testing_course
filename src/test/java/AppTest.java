

import jdk.jfr.Description;
import org.fluentlenium.adapter.FluentTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.sql2o.*;
import org.junit.*;
import spark.Spark;

import static org.assertj.core.api.Assertions.assertThat;

public class AppTest extends FluentTest {
  public WebDriver webDriver = new HtmlUnitDriver();

  @Rule
  public DatabaseRule database = new DatabaseRule();

  @BeforeEach
  public void DBsetUp() {
    DB.sql2o = new Sql2o("jdbc:postgresql://localhost:5432/pokedex_test", "postgres", "a");
  }
  @AfterEach
  protected void after() {
    try(Connection con = DB.sql2o.open()) {
      String deletePokemonsQuery = "DELETE FROM pokemons *;";
      String deleteMovesQuery = "DELETE FROM moves *;";
      String deleteMovesPokemonsQuery = "DELETE FROM moves_pokemons *;";
      con.createQuery(deletePokemonsQuery).executeUpdate();
      con.createQuery(deleteMovesQuery).executeUpdate();
      con.createQuery(deleteMovesPokemonsQuery).executeUpdate();
    }
  }

  @Override
  public WebDriver getDefaultDriver() {
    return webDriver;
  }

  @BeforeEach
  protected void before() {
    String[] args = {};
    App.main(args);
  }
  @AfterEach
  protected void afterAll() {
    Spark.stop();
  }

  @Test
  @Description("Opening the start page and Seeing if the Pokedex html id is present. Testing a small Unit => Unittest")
  public void rootTest() {
    goTo("http://localhost:4567/");
    assertThat(pageSource()).contains("Pokedex");
  }


  @Test
  @Description("Opens the start page then clicks on the ViewDex id and assures the loaded page contains Ivysaur and Charizard.Testing the integration  =>IntegrityTest")
  public void allPokemonPageIsDisplayed() {
    goTo("http://localhost:4567/");
    click("#viewDex");
    assertThat(pageSource().contains("Ivysaur"));
    assertThat(pageSource().contains("Charizard"));
  }

  @Test
  @Description("Opens  pokepage 6  and checks if Charizard is present thus checking the integrity of the page => IntegrityTest")
  public void individualPokemonPageIsDisplayed() {
    goTo("http://localhost:4567/pokepage/6");
    assertThat(pageSource().contains("Charizard"));
  }

  @Test
  @Description("Opens pokepage 6 and  clicks on the right glyphicon and expects Squirtle to appear. Testing multiple component => IntegrityTest")
  public void arrowsCycleThroughPokedexCorrectly() {
    goTo("http://localhost:4567/pokepage/6");
    click(".glyphicon-triangle-right");
    assertThat(pageSource().contains("Squirtle"));
  }

  @Test
  @Description("Testing the searchButton and sees if Charizard comes up. Testing only 1 unit therefor => UnitTest")
  public void searchResultsReturnMatches() {
    goTo("http://localhost:4567/pokedex");
    fill("#name").with("char");
    assertThat(pageSource().contains("Charizard"));
  }

  @Test
  @Description("Testing the search function of the pokedex and making sure fails  are marked properly. Testing only 1 unit => UnitTest")
  public void searchResultsReturnNoMatches() {
    goTo("http://localhost:4567/pokedex");
    fill("#name").with("x");
    assertThat(pageSource().contains("No matches for your search results"));
  }



}
