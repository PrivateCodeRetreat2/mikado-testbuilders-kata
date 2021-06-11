package com.murex.tbw.purchase;

import com.google.common.collect.ImmutableList;
import com.murex.tbw.domain.book.Author;
import com.murex.tbw.domain.book.Book;
import com.murex.tbw.domain.book.Genre;
import com.murex.tbw.domain.book.Novel;
import com.murex.tbw.domain.country.Country;
import com.murex.tbw.domain.country.Currency;
import com.murex.tbw.domain.country.Language;
import org.assertj.core.data.Offset;
import org.assertj.core.data.Percentage;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class InvoiceTest {
    @Test
    void No_Constraint_Applies_tax_rules_when_computing_total_amount() {
        // Instantiate an Invoice sent to USA
        Invoice invoice = new Invoice("foo", new Country("USA", Currency.US_DOLLAR, Language.ENGLISH));
        // Add it a purchased novel costing 50
        invoice.addPurchasedBook(new PurchasedBook(new Novel("A book", 50, new Author("authorName", new Country("USA", Currency.US_DOLLAR, Language.ENGLISH)), Language.ENGLISH, ImmutableList.of(Genre.ROMANCE)), 1));
        // Assert the total amount of the invoice is 56,35 : 15% of taxes plus a 2% reduction on novels
        assertThat(invoice.computeTotalAmount()).isCloseTo(56.35, Offset.offset(0.01));
    }


    public static class NovelTestDataBuilder {
        private double price = 10;

        public static NovelTestDataBuilder aNovel() {
            return new NovelTestDataBuilder();
        }

        public NovelTestDataBuilder costing(double price) {
            this.price = price;
            return this;
        }

        public Novel build() {
            return new Novel("Test Data Builders for Dummies", price, null, Language.ENGLISH, ImmutableList.of());
        }
    }

    public static class InvoiceTestDataBuilder {
        private Country country;
        private PurchasedBook purchasedBook;

        public static InvoiceTestDataBuilder anInvoice() {
            return new InvoiceTestDataBuilder();
        }

        public InvoiceTestDataBuilder from(Country country) {
            this.country = country;
            return this;
        }

        public InvoiceTestDataBuilder with(PurchasedBook purchasedBook) {
            this.purchasedBook = purchasedBook;
            return this;
        }

        public Invoice build() {
            Invoice invoice = new Invoice("someInvoice", country);
            invoice.addPurchasedBook(this.purchasedBook);
            return invoice;
        }
    }

    public static class PurchasedBookTestDataBuilder {
        private Book book;

        public static PurchasedBookTestDataBuilder aPurchasedBook() {
            return new PurchasedBookTestDataBuilder();
        }

        public PurchasedBookTestDataBuilder of(Book book) {
            this.book = book;
            return this;
        }

        public PurchasedBook build() {
            return new PurchasedBook(book, 1);
        }
    }

    @Test
    void Test_Data_Builders_Constraint_Applies_tax_rules_when_computing_total_amount() {
        // Using the Test Data Builder pattern:
        // Instantiate an Invoice sent to USA
        Invoice invoice = InvoiceTestDataBuilder.anInvoice()
                .from(new Country("USA", Currency.US_DOLLAR, Language.ENGLISH))
                .with(PurchasedBookTestDataBuilder.aPurchasedBook()
                        .of(NovelTestDataBuilder.aNovel()
                                .costing(50.0)
                                .build())
                        .build())
                .build();
        assertThat(50 * 1.15 * 0.98).isCloseTo(invoice.computeTotalAmount(), Offset.offset(0.01));
        // Add it a purchased novel costing 50
        // Assert the total amount of the invoice is 56,35 : 15% of taxes plus a 2% reduction on novels
    }

    @Test
    void Mikado_Method_Constraint_Applies_tax_rules_when_computing_total_amount() {
        // Using the Mikado method:
        // Instantiate an Invoice sent to USA
        // Add it a purchased novel costing 50
        // Assert the total amount of the invoice is 56,35 : 15% of taxes plus a 2% reduction on novels
    }

    @Test
    void Mikado_Method_And_Test_Data_Builders_Constraint_Applies_tax_rules_when_computing_total_amount() {
        // Using the Mikado method and the Test Data Builder pattern:
        // Instantiate an Invoice sent to USA
        // Add it a purchased novel costing 50
        // Assert the total amount of the invoice is 56,35 : 15% of taxes plus a 2% reduction on novels
    }
}
