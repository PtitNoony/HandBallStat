/*
 * Copyright (C) 2015 HAMON-KEROMEN A.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package fr.noony.handstats.utils;

/**
 *
 * @author Arnaud HAMON-KEROMEN
 */
public final class Translator {

    private static final String ERROR_STRING = "xxx";

    private static Language myLanguage;

    private Translator() {
        myLanguage = Language.FRANCAIS;
    }

    //INIT PAGE
    public static String getSelectedTeam() {
        switch (myLanguage) {
            case ENGLISH:
                return "Selected team";
            case FRANCAIS:
                return "Équipe sélectionnée :";
            default:
                return ERROR_STRING;
        }
    }

    public static String getCreateTeam() {
        switch (myLanguage) {
            case ENGLISH:
                return "Create a new team";
            case FRANCAIS:
                return "Créer une nouvelle équipe";
            default:
                return ERROR_STRING;
        }
    }

    public static String getManageTeam() {
        switch (myLanguage) {
            case ENGLISH:
                return "Manage team";
            case FRANCAIS:
                return "Gestion de l'équipe";
            default:
                return ERROR_STRING;
        }
    }

    public static String getChangeTeam() {
        switch (myLanguage) {
            case ENGLISH:
                return "Change team";
            case FRANCAIS:
                return "Changer d'équipe";
            default:
                return ERROR_STRING;
        }
    }

    // Creation page
    public static String getTeamCreation() {
        switch (myLanguage) {
            case ENGLISH:
                return "Team Creation";
            case FRANCAIS:
                return "Création d'une équipe";
            default:
                return ERROR_STRING;
        }
    }
}
