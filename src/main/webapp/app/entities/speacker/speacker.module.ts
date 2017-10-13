import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { CfpSharedModule } from '../../shared';
import {
    SpeackerService,
    SpeackerPopupService,
    SpeackerComponent,
    SpeackerDetailComponent,
    SpeackerDialogComponent,
    SpeackerPopupComponent,
    SpeackerDeletePopupComponent,
    SpeackerDeleteDialogComponent,
    speackerRoute,
    speackerPopupRoute,
} from './';

const ENTITY_STATES = [
    ...speackerRoute,
    ...speackerPopupRoute,
];

@NgModule({
    imports: [
        CfpSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        SpeackerComponent,
        SpeackerDetailComponent,
        SpeackerDialogComponent,
        SpeackerDeleteDialogComponent,
        SpeackerPopupComponent,
        SpeackerDeletePopupComponent,
    ],
    entryComponents: [
        SpeackerComponent,
        SpeackerDialogComponent,
        SpeackerPopupComponent,
        SpeackerDeleteDialogComponent,
        SpeackerDeletePopupComponent,
    ],
    providers: [
        SpeackerService,
        SpeackerPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class CfpSpeackerModule {}
