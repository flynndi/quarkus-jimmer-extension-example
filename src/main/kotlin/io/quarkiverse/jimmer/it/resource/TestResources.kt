package io.quarkiverse.jimmer.it.resource

import io.quarkiverse.jimmer.it.entity.*
import io.quarkiverse.jimmer.it.entity.dto.BookDetailView
import io.quarkiverse.jimmer.it.entity.dto.UserRoleInput
import io.quarkiverse.jimmer.it.repository.BookRepository
import io.quarkiverse.jimmer.it.repository.BookStoreRepository
import io.quarkiverse.jimmer.it.repository.UserRoleRepository
import io.quarkiverse.jimmer.runtime.Jimmer
import io.quarkiverse.jimmer.runtime.repository.common.Sort
import io.quarkiverse.jimmer.runtime.repository.support.Pagination
import io.quarkus.agroal.DataSource
import io.smallrye.common.annotation.Blocking
import jakarta.transaction.Transactional
import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import org.babyfish.jimmer.Page
import org.babyfish.jimmer.client.FetchBy
import org.babyfish.jimmer.client.meta.Api
import org.babyfish.jimmer.sql.ast.mutation.DeleteMode
import org.babyfish.jimmer.sql.ast.mutation.SaveMode
import org.babyfish.jimmer.sql.kt.fetcher.newFetcher
import org.jboss.resteasy.reactive.RestQuery
import java.util.*

@Path("/testResources")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Api("test")
class TestResources(

    private val bookRepository: BookRepository,

    private val bookStoreRepository: BookStoreRepository,

    @DataSource("DB2")
    private val userRoleRepository: UserRoleRepository
) {

    @GET
    @Path("/test")
    @Api
    fun test(): Response {
        val books = Jimmer.getDefaultKSqlClient()
            .createQuery(Book::class) {
                select(
                    table.fetchBy {
                        allScalarFields()
                        store {
                            allScalarFields()
                        }
                        authors {
                            allScalarFields()
                        }
                    }
                )
            }
            .execute()
        return Response.ok(books).build()
    }

    @GET
    @Path("/testBookRepository")
    @Api
    fun testBookRepository(): Response {
        return Response.ok(bookRepository.findAll()).build()
    }

    @GET
    @Path("/testBookStoreRepository")
    @Api
    fun testRepository(): Response {
        return Response.ok(bookStoreRepository.findAll()).build()
    }

    @POST
    @Path("/testBookRepositoryPage")
    @Api
    fun testBookRepositoryPage(pagination: Pagination): Response {
        return Response.ok(bookRepository.findAll(pagination)).build()
    }

    @POST
    @Path("/testBookRepositoryPageOther")
    @Api
    fun testBookRepositoryPageOther(pagination: Pagination): Response {
        return Response.ok(bookRepository.findAll(pagination.index, pagination.size)).build()
    }

    @POST
    @Path("/testBookRepositoryPageSort")
    @Api
    fun testBookRepositoryPageSort(pagination: Pagination): Response {
        return Response.ok(
            bookRepository.findAll(
                pagination.index,
                pagination.size,
                null,
                Sort.by(Sort.Direction.DESC, "id")
            )
        ).build()
    }

    @POST
    @Path("/testBookRepositoryPageFetcher")
    @Api
    fun testBookRepositoryPageFetcher(pagination: Pagination?): Page<Book> {
        return bookRepository.findAll(pagination!!, COMPLEX_BOOK)
    }

    @GET
    @Path("/testBookRepositoryById")
    @Api
    fun testBookRepositoryById(@RestQuery id: Long): Response {
        return Response.ok(bookRepository.findNullable(id)).build()
    }

    @GET
    @Path("/testBookRepositoryByIdOptional")
    @Api
    fun testBookRepositoryByIdOptional(@RestQuery id: Long): Response {
        return if (bookRepository.findById(id).isPresent) {
            Response.ok(bookRepository.findById(id).get()).build()
        } else {
            Response.noContent().build()
        }
    }

    @GET
    @Path("/testBookRepositoryByIdFetcher")
    @Api
    fun testBookRepositoryByIdFetcher(@RestQuery id: Long): @FetchBy("COMPLEX_BOOK") Book? {
        return bookRepository.findNullable(id, COMPLEX_BOOK)
    }

    @GET
    @Path("/testBookRepositoryByIdFetcherOptional")
    @Api
    fun testBookRepositoryByIdFetcherOptional(@RestQuery id: Long): @FetchBy("COMPLEX_BOOK") Book? {
        return if (bookRepository.findById(id, COMPLEX_BOOK).isPresent) {
            bookRepository.findById(id, COMPLEX_BOOK).get()
        } else {
            null
        }
    }

    @GET
    @Path("/testBookRepositoryViewById")
    @Api
    fun testBookRepositoryViewById(@RestQuery id: Long): Response {
        return Response.ok(bookRepository.viewer(BookDetailView::class).findNullable(id)).build()
    }

    @POST
    @Path("/testBookRepositoryFindAllById")
    @Api
    fun testBookRepositoryFindAllById(ids: List<Long>): Response {
        return Response.ok(bookRepository.findAllById(ids)).build()
    }

    @POST
    @Path("/testBookRepositoryFindByIdsFetcher")
    @Api
    fun testBookRepositoryFindByIdsFetcher(ids: List<Long>): Response {
        return Response.ok(bookRepository.findByIds(ids, COMPLEX_BOOK)).build()
    }

    @POST
    @Path("/testBookRepositoryFindMapByIds")
    @Api
    fun testBookRepositoryFindMapByIds(ids: List<Long>): Response {
        return Response.ok(bookRepository.findMapByIds(ids)).build()
    }

    @POST
    @Path("/testBookRepositoryFindMapByIdsFetcher")
    @Api
    fun testBookRepositoryFindMapByIdsFetcher(ids: List<Long>): Response {
        return Response.ok(bookRepository.findMapByIds(ids, COMPLEX_BOOK)).build()
    }

    @GET
    @Path("/testBookRepositoryFindAll")
    @Api
    fun testBookRepositoryFindAll(): Response {
        return Response.ok(bookRepository.findAll()).build()
    }

    @GET
    @Path("/testBookRepositoryFindAllSort")
    @Api
    fun testBookRepositoryFindAllSort(): Response {
        return Response.ok(bookRepository.findAll(Sort.by(Sort.Order.desc("name")))).build()
    }

    @GET
    @Path("/testBookRepositoryFindAllFetcherSort")
    @Api
    fun testBookRepositoryFindAllFetcherSort(): Response {
        return Response.ok(bookRepository.findAll(COMPLEX_BOOK, Sort.by(Sort.Order.desc("name")))).build()
    }

    @POST
    @Path("/testBookRepositoryFindAllPageFetcher")
    @Api
    fun testBookRepositoryFindAllPageFetcher(pagination: Pagination): Response {
        return Response.ok(bookRepository.findAll(pagination.index, pagination.size, COMPLEX_BOOK)).build()
    }

    @POST
    @Path("/testBookRepositoryFindAllPageSort")
    @Api
    fun testBookRepositoryFindAllPageSort(pagination: Pagination): Response {
        return Response.ok(bookRepository.findAll(pagination.index, pagination.size, null, Sort.by(Sort.Order.desc("name"))))
            .build()
    }

    @POST
    @Path("/testBookRepositoryFindAllPageFetcherSort")
    @Api
    fun testBookRepositoryFindAllPageFetcherSort(pagination: Pagination): Response {
        return Response
            .ok(
                bookRepository.findAll(
                    pagination.index,
                    pagination.size,
                    COMPLEX_BOOK,
                    Sort.by(Sort.Order.desc("name"))
                )
            )
            .build()
    }

    @GET
    @Path("/testBookRepositoryExistsById")
    @Api
    fun testBookRepositoryExistsById(@RestQuery id: Long): Response {
        return Response.ok(bookRepository.existsById(id)).build()
    }

    @GET
    @Path("/testBookRepositoryCount")
    @Api
    fun testBookRepositoryCount(): Response {
        return Response.ok(bookRepository.count()).build()
    }

    @POST
    @Path("/testUserRoleRepositoryInsert")
    @Api
    fun testUserRoleRepositoryInsert(userRole: UserRole?): Response {
        return Response.ok(userRoleRepository.insert(userRole!!)).build()
    }

    @POST
    @Path("/testUserRoleRepositoryInsertInput")
    @Transactional(rollbackOn = [Exception::class])
    @Api
    fun testUserRoleRepositoryInsertInput(userRoleInput: UserRoleInput?): Response {
        return Response.ok(userRoleRepository.insert(userRoleInput!!)).build()
    }

    @POST
    @Path("/testUserRoleRepositorySave")
    @Transactional(rollbackOn = [java.lang.Exception::class])
    @Api
    fun testUserRoleRepositorySave(userRole: UserRole): Response {
        return Response.ok(userRoleRepository.save(userRole)).build()
    }

    @POST
    @Path("/testUserRoleRepositorySaveInput")
    @Transactional(rollbackOn = [java.lang.Exception::class])
    @Api
    fun testUserRoleRepositorySaveInput(userRoleInput: UserRoleInput?): Response {
        return Response.ok(userRoleRepository.save(userRoleInput!!)).build()
    }

    @POST
    @Path("/testUserRoleRepositorySaveInputSaveMode")
    @Transactional(rollbackOn = [java.lang.Exception::class])
    @Api
    fun testUserRoleRepositorySaveInputSaveMode(userRoleInput: UserRoleInput?): Response {
        return Response.ok(userRoleRepository.save(userRoleInput!!, SaveMode.INSERT_ONLY)).build()
    }

    @POST
    @Path("/testUserRoleRepositorySaveEntities")
    @Transactional(rollbackOn = [java.lang.Exception::class])
    @Api
    fun testUserRoleRepositorySaveEntities(list: List<UserRole>): Response {
        return Response.ok(userRoleRepository.saveEntities(list).simpleResults).build()
    }

    @POST
    @Path("/testUserRoleRepositorySaveEntitiesSaveMode")
    @Transactional(rollbackOn = [java.lang.Exception::class])
    @Api
    fun testUserRoleRepositorySaveEntitiesSaveMode(list: List<UserRole>): Response {
        return Response.ok(userRoleRepository.saveEntities(list, SaveMode.INSERT_ONLY)).build()
    }

    @DELETE
    @Path("/testUserRoleRepositoryDeleteAll")
    @Transactional(rollbackOn = [java.lang.Exception::class])
    @Api
    fun testUserRoleRepositoryDeleteAll(list: List<UserRole>): Response {
        return Response.ok(userRoleRepository.deleteAll(list, DeleteMode.AUTO)).build()
    }

    @POST
    @Path("/testUserRoleRepositoryUpdate")
    @Transactional(rollbackOn = [java.lang.Exception::class])
    @Api
    fun testUserRoleRepositoryUpdate(userRole: UserRole?): Response {
        return Response.ok(userRoleRepository.update(userRole!!)).build()
    }

    @GET
    @Path("/testUserRoleRepositoryById")
    @Api
    fun UserRoleRepositoryById(@RestQuery id: UUID?): Response {
        return Response.ok(userRoleRepository.findNullable(id!!)).build()
    }

    @PUT
    @Path("/testUserRoleRepositoryUpdateInput")
    @Transactional(rollbackOn = [java.lang.Exception::class])
    @Api
    fun testUserRoleRepositoryUpdateInput(userRoleInput: UserRoleInput): Response {
        userRoleRepository.update(userRoleInput)
        return Response.ok().build()
    }

    @POST
    @Path("/testBookRepositoryFindByIdsView")
    @Api
    fun testBookRepositoryFindByIdsView(ids: List<Long>): Response {
        return Response.ok(bookRepository.viewer(BookDetailView::class).findByIds(ids)).build()
    }

    @GET
    @Path("/testBookRepositoryFindAllView")
    @Api
    fun testBookRepositoryFindAllView(): Response {
        return Response.ok(bookRepository.viewer(BookDetailView::class).findAll()).build()
    }

    @GET
    @Path("/testBookRepositoryFindAllSortView")
    @Api
    fun testBookRepositoryFindAllSortView(): Response {
        return Response.ok(bookRepository.viewer(BookDetailView::class).findAll(Sort.by(Sort.Order.desc("name"))))
            .build()
    }

    @POST
    @Path("/testBookRepositoryFindAllPageView")
    @Api
    fun testBookRepositoryFindAllPageView(pagination: Pagination): Response {
        return Response.ok(bookRepository.viewer(BookDetailView::class).findAll(pagination.index, pagination.size))
            .build()
    }

    @POST
    @Path("/testBookRepositoryFindAllPageSortView")
    @Api
    fun testBookRepositoryFindAllPageSortView(pagination: Pagination): Response {
        return Response.ok(
            bookRepository.viewer(BookDetailView::class).findAll(
                pagination.index, pagination.size,
                Sort.by(Sort.Order.desc("name"))
            )
        ).build()
    }

    @GET
    @Path("/testBookRepositoryCustomQuery")
    @Api
    fun testBookRepositoryCustomQuery(@RestQuery id: Long): Response {
        return Response.ok(bookRepository.selectBookById(id)).build()
    }

    @POST
    @Path("/testBookRepositoryFindMapByIdsView")
    @Api
    fun testBookRepositoryFindMapByIdsView(ids: List<Long>): Response {
        return Response.ok(bookRepository.viewer(BookDetailView::class).findMapByIds(ids)).build()
    }

    companion object {

        private val COMPLEX_BOOK =
            newFetcher(Book::class).by {

                allScalarFields()

                store {
                    name()
                }

                authors {
                    firstName()
                    lastName()
                }
            }
    }
}