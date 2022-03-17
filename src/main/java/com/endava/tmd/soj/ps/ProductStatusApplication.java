package com.endava.tmd.soj.ps;

import java.io.File;

import com.endava.tmd.soj.ps.service.ApplicationService;
import com.endava.tmd.soj.ps.service.ContentRetrieverService;
import com.endava.tmd.soj.ps.service.EmagContentReaderService;
import com.endava.tmd.soj.ps.service.ExcelService;
import com.endava.tmd.soj.ps.service.UserPreferencesLoaderService;

public class ProductStatusApplication {

    public static void main(final String[] args) {
        if (args.length == 0) {
            System.out.println("In order to use this program, please pass an argument representing the name of the input file");
            return;
        }

        final var userPrefLoaderService = new UserPreferencesLoaderService();
        final var contentReaderService = new EmagContentReaderService();
        final var contentRetrieverService = new ContentRetrieverService(contentReaderService);
        final var excelService = new ExcelService();
        final var applicationService = new ApplicationService(userPrefLoaderService, contentRetrieverService, excelService);
        applicationService.execute(new File(args[0]));
    }

}
