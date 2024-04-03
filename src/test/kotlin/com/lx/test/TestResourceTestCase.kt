package com.lx.test

import io.netty.handler.codec.http.HttpHeaderValues
import io.quarkiverse.jimmer.it.repository.BookRepository
import io.quarkiverse.jimmer.it.repository.BookStoreRepository
import io.quarkiverse.jimmer.it.repository.UserRoleRepository
import io.quarkus.agroal.DataSource
import io.quarkus.agroal.DataSource.DataSourceLiteral
import io.quarkus.arc.Arc
import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured
import io.restassured.http.Header
import io.vertx.core.http.HttpHeaders
import jakarta.enterprise.inject.Default
import jakarta.inject.Inject
import org.apache.http.HttpStatus
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.util.*

@QuarkusTest
class TestResourceTestCase {

    @Inject
    @field:Default
    lateinit var bookRepository: BookRepository

    @Inject
    @field:DataSource("DB2")
    lateinit var userRoleRepository: UserRoleRepository

    @Test
    fun testRepository() {
        val bookRepository = Arc.container().instance(BookRepository::class.java).get()
        val bookStoreRepository = Arc.container().instance(BookStoreRepository::class.java).get()
        val userRoleRepository = Arc.container()
            .instance(UserRoleRepository::class.java, DataSourceLiteral("DB2")).get()
        Assertions.assertNotNull(bookRepository)
        Assertions.assertNotNull(bookStoreRepository)
        Assertions.assertEquals(bookRepository, this.bookRepository)
        Assertions.assertEquals(userRoleRepository, this.userRoleRepository)
    }

    @Test
    fun testPage() {
        val body = """
                {
                    "index": 0,
                    "size": 1
                }
                
                """.trimIndent()
        val response = RestAssured.given()
            .body(body)
            .header(Header(HttpHeaders.CONTENT_TYPE.toString(), HttpHeaderValues.APPLICATION_JSON.toString()))
            .log()
            .all()
            .`when`()
            .post("testResources/testBookRepositoryPage")
        val responseJsonPath = response.jsonPath()
        Assertions.assertEquals(6, responseJsonPath.getInt("totalRowCount"))
        Assertions.assertEquals(6, responseJsonPath.getInt("totalPageCount"))
    }

    @Test
    fun testPageOther() {
        val body = """
                {
                    "index": 0,
                    "size": 1
                }
                
                """.trimIndent()
        val response = RestAssured.given()
            .body(body)
            .header(Header(HttpHeaders.CONTENT_TYPE.toString(), HttpHeaderValues.APPLICATION_JSON.toString()))
            .log()
            .all()
            .`when`()
            .post("testResources/testBookRepositoryPageOther")
        val responseJsonPath = response.jsonPath()
        Assertions.assertEquals(6, responseJsonPath.getInt("totalRowCount"))
        Assertions.assertEquals(6, responseJsonPath.getInt("totalPageCount"))
    }

    @Test
    fun testBookRepositoryPageSort() {
        val body = """
                {
                    "index": 0,
                    "size": 1
                }
                
                """.trimIndent()
        val response = RestAssured.given()
            .body(body)
            .header(Header(HttpHeaders.CONTENT_TYPE.toString(), HttpHeaderValues.APPLICATION_JSON.toString()))
            .log()
            .all()
            .`when`()
            .post("testResources/testBookRepositoryPageSort")
        val responseJsonPath = response.jsonPath()
        Assertions.assertEquals(6, responseJsonPath.getInt("totalRowCount"))
        Assertions.assertEquals(6, responseJsonPath.getInt("totalPageCount"))
        Assertions.assertEquals(11, responseJsonPath.getLong("rows[0].id"))
    }

    @Test
    fun testPageFetcher() {
        val body = """
                {
                    "index": 0,
                    "size": 1
                }
                
                """.trimIndent()
        val response = RestAssured.given()
            .body(body)
            .header(Header(HttpHeaders.CONTENT_TYPE.toString(), HttpHeaderValues.APPLICATION_JSON.toString()))
            .log()
            .all()
            .`when`()
            .post("testResources/testBookRepositoryPageFetcher")
        val responseJsonPath = response.jsonPath()
        Assertions.assertEquals(6, responseJsonPath.getInt("totalRowCount"))
        Assertions.assertEquals(6, responseJsonPath.getInt("totalPageCount"))
        Assertions.assertNotNull(responseJsonPath.getList<Any>("rows"))
        Assertions.assertNotNull(responseJsonPath.get("rows.authors"))
    }

    @Test
    fun testBookRepositoryById() {
        val response = RestAssured.given()
            .queryParam("id", 1L)
            .log()
            .all()
            .`when`()["testResources/testBookRepositoryById"]
        Assertions.assertNotNull(response.jsonPath())
    }

    @Test
    fun testBookRepositoryByIdOptionalPresent() {
        val response = RestAssured.given()
            .queryParam("id", 1L)
            .log()
            .all()
            .`when`()["testResources/testBookRepositoryByIdOptional"]
        Assertions.assertNotNull(response.jsonPath())
    }

    @Test
    fun testBookRepositoryByIdOptionalEmpty() {
        val response = RestAssured.given()
            .queryParam("id", 0)
            .log()
            .all()
            .`when`()["testResources/testBookRepositoryByIdOptional"]
        Assertions.assertEquals(HttpStatus.SC_NO_CONTENT, response.statusCode())
    }

    @Test
    fun testBookRepositoryByIdFetcher() {
        val response = RestAssured.given()
            .queryParam("id", 0)
            .log()
            .all()
            .`when`()["testResources/testBookRepositoryByIdFetcher"]
        Assertions.assertEquals(response.statusCode(), HttpStatus.SC_NO_CONTENT)
    }

    @Test
    fun testBookRepositoryByIdFetcherOptionalPresent() {
        val response = RestAssured.given()
            .queryParam("id", 1L)
            .log()
            .all()
            .`when`()["testResources/testBookRepositoryByIdFetcherOptional"]
        Assertions.assertNotNull(response.jsonPath())
    }

    @Test
    fun testBookRepositoryByIdFetcherOptionalEmpty() {
        val response = RestAssured.given()
            .queryParam("id", 0)
            .log()
            .all()
            .`when`()["testResources/testBookRepositoryByIdFetcherOptional"]
        Assertions.assertEquals(response.body().print(), "")
    }

    @Test
    fun testBookRepositoryViewById() {
        val response = RestAssured.given()
            .queryParam("id", 1L)
            .log()
            .all()
            .`when`()["testResources/testBookRepositoryViewById"]
        Assertions.assertNotNull(response.jsonPath())
        Assertions.assertEquals(1, response.jsonPath().getLong("id"))
        Assertions.assertNotNull(response.jsonPath().getJsonObject("store"))
        Assertions.assertNotNull(response.jsonPath().getJsonObject("authors"))
    }

    @Test
    fun testBookRepositoryFindAllById() {
        val body = """
                [1, 2]
                
                """.trimIndent()
        val response = RestAssured.given()
            .header(Header(HttpHeaders.CONTENT_TYPE.toString(), HttpHeaderValues.APPLICATION_JSON.toString()))
            .body(body)
            .log()
            .all()
            .`when`()
            .post("testResources/testBookRepositoryFindAllById")
        Assertions.assertNotNull(response.jsonPath())
        Assertions.assertEquals(1, response.jsonPath().getLong("[0].id"))
    }

    @Test
    fun testBookRepositoryFindByIdsFetcher() {
        val body = """
                [1, 2]
                
                """.trimIndent()
        val response = RestAssured.given()
            .header(Header(HttpHeaders.CONTENT_TYPE.toString(), HttpHeaderValues.APPLICATION_JSON.toString()))
            .body(body)
            .log()
            .all()
            .`when`()
            .post("testResources/testBookRepositoryFindByIdsFetcher")
        Assertions.assertNotNull(response.jsonPath())
        Assertions.assertEquals(1, response.jsonPath().getLong("[0].id"))
        Assertions.assertEquals(2, response.jsonPath().getLong("[0].authors[0].id"))
    }

    @Test
    fun testBookRepositoryFindMapByIds() {
        val body = """
                [1, 2]
                
                """.trimIndent()
        val response = RestAssured.given()
            .header(Header(HttpHeaders.CONTENT_TYPE.toString(), HttpHeaderValues.APPLICATION_JSON.toString()))
            .body(body)
            .log()
            .all()
            .`when`()
            .post("testResources/testBookRepositoryFindMapByIds")
        Assertions.assertNotNull(response.jsonPath().getMap<Any, Any>(""))
    }

    @Test
    fun testBookRepositoryFindMapByIdsFetcher() {
        val body = """
                [1, 2]
                
                """.trimIndent()
        val response = RestAssured.given()
            .header(Header(HttpHeaders.CONTENT_TYPE.toString(), HttpHeaderValues.APPLICATION_JSON.toString()))
            .body(body)
            .log()
            .all()
            .`when`()
            .post("testResources/testBookRepositoryFindMapByIdsFetcher")
        Assertions.assertNotNull(response.jsonPath().getMap<Any, Any>(""))
        Assertions.assertNotNull(response.jsonPath().getMap<Any, Any>("")["1"])
    }

    @Test
    fun testBookRepositoryFindAll() {
        val response = RestAssured.given()
            .log()
            .all()
            .`when`()["testResources/testBookRepositoryFindAll"]
        Assertions.assertNotNull(response.jsonPath())
        Assertions.assertEquals(1, response.jsonPath().getLong("[0].id"))
    }

    @Test
    fun testBookRepositoryFindAllSort() {
        val response = RestAssured.given()
            .log()
            .all()
            .`when`()["testResources/testBookRepositoryFindAllSort"]
        Assertions.assertNotNull(response.jsonPath())
        Assertions.assertEquals("Programming TypeScript", response.jsonPath().getString("[0].name"))
    }

    @Test
    fun testBookRepositoryFindAllFetcherSort() {
        val response = RestAssured.given()
            .log()
            .all()
            .`when`()["testResources/testBookRepositoryFindAllFetcherSort"]
        Assertions.assertNotNull(response.jsonPath())
        Assertions.assertEquals("Programming TypeScript", response.jsonPath().getString("[0].name"))
        Assertions.assertNotNull(response.jsonPath().getString("[0].authors"))
        Assertions.assertNotNull(response.jsonPath().getString("[0].store"))
    }

    @Test
    fun testBookRepositoryFindAllPageFetcher() {
        val body = """
                {
                    "index": 0,
                    "size": 1
                }
                
                """.trimIndent()
        val response = RestAssured.given()
            .header(Header(HttpHeaders.CONTENT_TYPE.toString(), HttpHeaderValues.APPLICATION_JSON.toString()))
            .body(body)
            .log()
            .all()
            .`when`()
            .post("testResources/testBookRepositoryFindAllPageFetcher")
        Assertions.assertNotNull(response.jsonPath())
        Assertions.assertNotNull(response.jsonPath().getString("rows[0].authors"))
        Assertions.assertEquals(6, response.jsonPath().getInt("totalRowCount"))
        Assertions.assertNotNull(response.jsonPath().getString("totalPageCount"))
    }

    @Test
    fun testBookRepositoryFindAllPageSort() {
        val body = """
                {
                    "index": 0,
                    "size": 1
                }
                
                """.trimIndent()
        val response = RestAssured.given()
            .header(Header(HttpHeaders.CONTENT_TYPE.toString(), HttpHeaderValues.APPLICATION_JSON.toString()))
            .body(body)
            .log()
            .all()
            .`when`()
            .post("testResources/testBookRepositoryFindAllPageSort")
        Assertions.assertNotNull(response.jsonPath())
        Assertions.assertEquals("Programming TypeScript", response.jsonPath().getString("rows[0].name"))
        Assertions.assertEquals(6, response.jsonPath().getInt("totalRowCount"))
        Assertions.assertNotNull(response.jsonPath().getString("totalPageCount"))
    }

    @Test
    fun testBookRepositoryFindAllPageFetcherSort() {
        val body = """
                {
                    "index": 0,
                    "size": 1
                }
                
                """.trimIndent()
        val response = RestAssured.given()
            .header(Header(HttpHeaders.CONTENT_TYPE.toString(), HttpHeaderValues.APPLICATION_JSON.toString()))
            .body(body)
            .log()
            .all()
            .`when`()
            .post("testResources/testBookRepositoryFindAllPageFetcherSort")
        Assertions.assertNotNull(response.jsonPath())
        Assertions.assertEquals("Programming TypeScript", response.jsonPath().getString("rows[0].name"))
        Assertions.assertNotNull(response.jsonPath().getString("rows[0].authors"))
        Assertions.assertEquals(6, response.jsonPath().getInt("totalRowCount"))
        Assertions.assertNotNull(response.jsonPath().getString("totalPageCount"))
    }

    @Test
    fun testBookRepositoryExistsById() {
        val response = RestAssured.given()
            .queryParam("id", 0)
            .log()
            .all()
            .`when`()["testResources/testBookRepositoryExistsById"]
        Assertions.assertFalse(response.jsonPath().getBoolean(""))
    }

    @Test
    fun testBookRepositoryCount() {
        val response = RestAssured.given()
            .log()
            .all()
            .`when`()["testResources/testBookRepositoryCount"]
        Assertions.assertEquals(6, response.jsonPath().getInt(""))
    }

    @Test
    fun testUserRoleRepositoryInsert() {
        val body = """
                {
                     "id": "029253C4-35D3-F78B-5A21-E12D7F358A0B",
                     "userId": "12",
                     "roleId": "213",
                     "deleteFlag": false
                 }
                
                """.trimIndent()
        val response = RestAssured.given()
            .header(Header(HttpHeaders.CONTENT_TYPE.toString(), HttpHeaderValues.APPLICATION_JSON.toString()))
            .body(body)
            .log()
            .all()
            .`when`()
            .post("testResources/testUserRoleRepositoryInsert")
        println("response.body().prettyPrint() = " + response.body().prettyPrint())
        Assertions.assertEquals("029253c4-35d3-f78b-5a21-e12d7f358a0b", response.jsonPath().getString("id"))
        Assertions.assertEquals("12", response.jsonPath().getString("userId"))
        Assertions.assertEquals("213", response.jsonPath().getString("roleId"))
        Assertions.assertFalse(response.jsonPath().getBoolean("deleteFlag"))
    }

    @Test
    fun testUserRoleRepositoryInsertInput() {
        val body = """
                {
                     "id": "81D8F7AB-C3FB-A8B6-3B22-C20A26C83B72",
                     "userId": "12",
                     "roleId": "213",
                     "deleteFlag": false
                 }
                
                """.trimIndent()
        val response = RestAssured.given()
            .header(Header(HttpHeaders.CONTENT_TYPE.toString(), HttpHeaderValues.APPLICATION_JSON.toString()))
            .body(body)
            .log()
            .all()
            .`when`()
            .post("testResources/testUserRoleRepositoryInsertInput")
        Assertions.assertEquals("81d8f7ab-c3fb-a8b6-3b22-c20a26c83b72", response.jsonPath().getString("id"))
        Assertions.assertEquals("12", response.jsonPath().getString("userId"))
        Assertions.assertEquals("213", response.jsonPath().getString("roleId"))
        Assertions.assertFalse(response.jsonPath().getBoolean("deleteFlag"))
    }

    @Test
    fun testUserRoleRepositorySave() {
        val body = """
                {
                     "id": "0C844055-A86E-94D9-2C50-77CAFBBC20AB",
                     "userId": "12",
                     "roleId": "213",
                     "deleteFlag": false
                 }
                
                """.trimIndent()
        val response = RestAssured.given()
            .header(Header(HttpHeaders.CONTENT_TYPE.toString(), HttpHeaderValues.APPLICATION_JSON.toString()))
            .body(body)
            .log()
            .all()
            .`when`()
            .post("testResources/testUserRoleRepositorySave")
        Assertions.assertEquals("0c844055-a86e-94d9-2c50-77cafbbc20ab", response.jsonPath().getString("id"))
        Assertions.assertEquals("12", response.jsonPath().getString("userId"))
        Assertions.assertEquals("213", response.jsonPath().getString("roleId"))
        Assertions.assertFalse(response.jsonPath().getBoolean("deleteFlag"))
    }

    @Test
    fun testUserRoleRepositorySaveInput() {
        val body = """
                {
                     "id": "EEB27AAA-8FEA-4177-0179-183FCB154B36",
                     "userId": "12",
                     "roleId": "213",
                     "deleteFlag": false
                 }
                
                """.trimIndent()
        val response = RestAssured.given()
            .header(Header(HttpHeaders.CONTENT_TYPE.toString(), HttpHeaderValues.APPLICATION_JSON.toString()))
            .body(body)
            .log()
            .all()
            .`when`()
            .post("testResources/testUserRoleRepositorySaveInput")
        Assertions.assertEquals("eeb27aaa-8fea-4177-0179-183fcb154b36", response.jsonPath().getString("id"))
        Assertions.assertEquals("12", response.jsonPath().getString("userId"))
        Assertions.assertEquals("213", response.jsonPath().getString("roleId"))
        Assertions.assertFalse(response.jsonPath().getBoolean("deleteFlag"))
    }

    @Test
    fun testUserRoleRepositorySaveInputSaveMode() {
        val body = """
                {
                     "id": "E85FC166-66DD-F496-F733-22BA38DC807D",
                     "userId": "12",
                     "roleId": "213",
                     "deleteFlag": false
                 }
                
                """.trimIndent()
        val response = RestAssured.given()
            .header(Header(HttpHeaders.CONTENT_TYPE.toString(), HttpHeaderValues.APPLICATION_JSON.toString()))
            .body(body)
            .log()
            .all()
            .`when`()
            .post("testResources/testUserRoleRepositorySaveInputSaveMode")
        Assertions.assertEquals(1, response.jsonPath().getInt("totalAffectedRowCount"))
        Assertions.assertEquals(
            "e85fc166-66dd-f496-f733-22ba38dc807d",
            response.jsonPath().getString("originalEntity.id")
        )
        Assertions.assertEquals(
            "e85fc166-66dd-f496-f733-22ba38dc807d",
            response.jsonPath().getString("modifiedEntity.id")
        )
        Assertions.assertFalse(response.jsonPath().getBoolean("modified"))
    }

    @Test
    fun testUserRoleRepositorySaveEntities() {
        val body = """
                [
                      {
                          "id": "D45493FF-5770-C90D-CFFF-DA11A8C07264",
                          "userId": "12",
                          "roleId": "213",
                          "deleteFlag": false
                      },
                      {
                          "id": "AC3CEADF-151E-BD7E-2D73-D50E5F86B31D",
                          "userId": "333",
                          "roleId": "333",
                          "deleteFlag": false
                      }
                  ]
                
                """.trimIndent()
        val response = RestAssured.given()
            .header(Header(HttpHeaders.CONTENT_TYPE.toString(), HttpHeaderValues.APPLICATION_JSON.toString()))
            .body(body)
            .log()
            .all()
            .`when`()
            .post("testResources/testUserRoleRepositorySaveEntities")
        Assertions.assertEquals(response.statusCode(), HttpStatus.SC_OK)
        Assertions.assertEquals("d45493ff-5770-c90d-cfff-da11a8c07264", response.jsonPath().getString("[0].modifiedEntity.id"))
        Assertions.assertEquals("ac3ceadf-151e-bd7e-2d73-d50e5f86b31d", response.jsonPath().getString("[1].modifiedEntity.id"))
    }

    @Test
    fun testUserRoleRepositorySaveEntitiesSaveMode() {
        val body = """
                [
                      {
                          "id": "4C1710D4-46E6-33D3-DC53-8492F6664050",
                          "userId": "12",
                          "roleId": "213",
                          "deleteFlag": false
                      },
                      {
                          "id": "10C40E99-B6EB-A6AE-B1C5-61FA87FEF236",
                          "userId": "333",
                          "roleId": "333",
                          "deleteFlag": false
                      }
                  ]
                
                """.trimIndent()
        val response = RestAssured.given()
            .header(Header(HttpHeaders.CONTENT_TYPE.toString(), HttpHeaderValues.APPLICATION_JSON.toString()))
            .body(body)
            .log()
            .all()
            .`when`()
            .post("testResources/testUserRoleRepositorySaveEntitiesSaveMode")
        Assertions.assertEquals(response.statusCode(), HttpStatus.SC_OK)
        Assertions.assertEquals(2, response.jsonPath().getInt("totalAffectedRowCount"))
        Assertions.assertEquals(2, response.jsonPath().getInt("simpleResults[0].totalAffectedRowCount"))
    }

    @Test
    fun testUserRoleRepositoryUpdate() {
        val body = """
                {
                     "id": "defc2d01-fb38-4d31-b006-fd182b25aa33",
                     "userId": "12",
                     "roleId": "213",
                     "deleteFlag": false
                 }
                
                """.trimIndent()
        val response = RestAssured.given()
            .header(Header(HttpHeaders.CONTENT_TYPE.toString(), HttpHeaderValues.APPLICATION_JSON.toString()))
            .body(body)
            .log()
            .all()
            .`when`()
            .post("testResources/testUserRoleRepositoryUpdate")
        Assertions.assertEquals("defc2d01-fb38-4d31-b006-fd182b25aa33", response.jsonPath().getString("id"))
        Assertions.assertEquals("12", response.jsonPath().getString("userId"))
        Assertions.assertEquals("213", response.jsonPath().getString("roleId"))
        Assertions.assertFalse(response.jsonPath().getBoolean("deleteFlag"))
    }

    @Test
    fun testUserRoleRepositoryById() {
        val response = RestAssured.given()
            .queryParam("id", UUID.fromString("defc2d01-fb38-4d31-b006-fd182b25aa33"))
            .log()
            .all()
            .`when`()["testResources/testUserRoleRepositoryById"]
        Assertions.assertNotNull(response.jsonPath())
        Assertions.assertEquals("defc2d01-fb38-4d31-b006-fd182b25aa33", response.jsonPath().getString("id"))
    }

    @Test
    fun testUserRoleRepositoryUpdateInput() {
        val body = """
                {
                    "id": "defc2d01-fb38-4d31-b006-fd182b25aa33",
                    "userId": "3",
                    "roleId": "4",
                    "deleteFlag": false
                }
                
                """.trimIndent()
        val response = RestAssured.given()
            .body(body)
            .header(Header(HttpHeaders.CONTENT_TYPE.toString(), HttpHeaderValues.APPLICATION_JSON.toString()))
            .log()
            .all()
            .`when`()
            .put("testResources/testUserRoleRepositoryUpdateInput")
        Assertions.assertEquals(response.statusCode, HttpStatus.SC_OK)
    }

    @Test
    fun testBookRepositoryFindByIdsView() {
        val body = """
                [1,3,5,7]
                
                """.trimIndent()
        val response = RestAssured.given()
            .body(body)
            .header(Header(HttpHeaders.CONTENT_TYPE.toString(), HttpHeaderValues.APPLICATION_JSON.toString()))
            .log()
            .all()
            .`when`()
            .post("testResources/testBookRepositoryFindByIdsView")
        Assertions.assertEquals(response.statusCode, HttpStatus.SC_OK)
        Assertions.assertNotNull(response.jsonPath().get("[0].store"))
        Assertions.assertNotNull(response.jsonPath().get("[0].authors"))
    }

    @Test
    fun testBookRepositoryFindAllView() {
        val response = RestAssured.given()
            .log()
            .all()
            .`when`()["testResources/testBookRepositoryFindAllView"]
        Assertions.assertEquals(response.statusCode, HttpStatus.SC_OK)
        Assertions.assertNotNull(response.jsonPath().get("[0].store"))
        Assertions.assertNotNull(response.jsonPath().get("[0].authors"))
    }

    @Test
    fun testBookRepositoryFindAllSortView() {
        val response = RestAssured.given()
            .log()
            .all()
            .`when`()["testResources/testBookRepositoryFindAllSortView"]
        Assertions.assertEquals(response.statusCode, HttpStatus.SC_OK)
        Assertions.assertEquals(7, response.jsonPath().getLong("[0].id"))
        Assertions.assertNotNull(response.jsonPath().get("[0].store"))
        Assertions.assertNotNull(response.jsonPath().get("[0].authors"))
    }

    @Test
    fun testBookRepositoryFindAllPageView() {
        val body = """
                {
                    "index": 0,
                    "size": 1
                }
                
                """.trimIndent()
        val response = RestAssured.given()
            .header(Header(HttpHeaders.CONTENT_TYPE.toString(), HttpHeaderValues.APPLICATION_JSON.toString()))
            .body(body)
            .log()
            .all()
            .`when`()
            .post("testResources/testBookRepositoryFindAllPageView")
        Assertions.assertEquals(response.statusCode, HttpStatus.SC_OK)
        Assertions.assertNotNull(response.jsonPath())
        Assertions.assertEquals("Learning GraphQL", response.jsonPath().getString("rows[0].name"))
        Assertions.assertNotNull(response.jsonPath().getString("rows[0].authors"))
        Assertions.assertEquals(6, response.jsonPath().getInt("totalRowCount"))
        Assertions.assertNotNull(response.jsonPath().getString("totalPageCount"))
    }

    @Test
    fun testBookRepositoryFindAllPageSortView() {
        val body = """
                {
                    "index": 0,
                    "size": 1
                }
                
                """.trimIndent()
        val response = RestAssured.given()
            .header(Header(HttpHeaders.CONTENT_TYPE.toString(), HttpHeaderValues.APPLICATION_JSON.toString()))
            .body(body)
            .log()
            .all()
            .`when`()
            .post("testResources/testBookRepositoryFindAllPageSortView")
        Assertions.assertEquals(response.statusCode, HttpStatus.SC_OK)
        Assertions.assertNotNull(response.jsonPath())
        Assertions.assertEquals("Programming TypeScript", response.jsonPath().getString("rows[0].name"))
        Assertions.assertNotNull(response.jsonPath().getString("rows[0].authors"))
        Assertions.assertEquals(6, response.jsonPath().getInt("totalRowCount"))
        Assertions.assertNotNull(response.jsonPath().getString("totalPageCount"))
    }

    @Test
    fun testBookRepositoryCustomQuery() {
        val response = RestAssured.given()
            .queryParam("id", 1L)
            .log()
            .all()
            .`when`()["testResources/testBookRepositoryCustomQuery"]
        Assertions.assertNotNull(response.jsonPath())
    }

    @Test
    fun testBookRepositoryFindMapByIdsView() {
        val body = """
                [1, 2]
                
                """.trimIndent()
        val response = RestAssured.given()
            .header(Header(HttpHeaders.CONTENT_TYPE.toString(), HttpHeaderValues.APPLICATION_JSON.toString()))
            .body(body)
            .log()
            .all()
            .`when`()
            .post("testResources/testBookRepositoryFindMapByIdsView")
        Assertions.assertNotNull(response.jsonPath().getMap<Any, Any>(""))
        Assertions.assertNotNull(response.jsonPath().get("1"))
    }

}