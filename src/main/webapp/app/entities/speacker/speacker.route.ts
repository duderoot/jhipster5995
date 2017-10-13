import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { SpeackerComponent } from './speacker.component';
import { SpeackerDetailComponent } from './speacker-detail.component';
import { SpeackerPopupComponent } from './speacker-dialog.component';
import { SpeackerDeletePopupComponent } from './speacker-delete-dialog.component';

export const speackerRoute: Routes = [
    {
        path: 'speacker',
        component: SpeackerComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'cfpApp.speacker.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'speacker/:id',
        component: SpeackerDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'cfpApp.speacker.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const speackerPopupRoute: Routes = [
    {
        path: 'speacker-new',
        component: SpeackerPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'cfpApp.speacker.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'speacker/:id/edit',
        component: SpeackerPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'cfpApp.speacker.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'speacker/:id/delete',
        component: SpeackerDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'cfpApp.speacker.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
