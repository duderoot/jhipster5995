import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService, JhiDataUtils } from 'ng-jhipster';

import { Speacker } from './speacker.model';
import { SpeackerPopupService } from './speacker-popup.service';
import { SpeackerService } from './speacker.service';

@Component({
    selector: 'jhi-speacker-dialog',
    templateUrl: './speacker-dialog.component.html'
})
export class SpeackerDialogComponent implements OnInit {

    speacker: Speacker;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private dataUtils: JhiDataUtils,
        private jhiAlertService: JhiAlertService,
        private speackerService: SpeackerService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
    }

    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }

    setFileData(event, entity, field, isImage) {
        this.dataUtils.setFileData(event, entity, field, isImage);
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.speacker.id !== undefined) {
            this.subscribeToSaveResponse(
                this.speackerService.update(this.speacker));
        } else {
            this.subscribeToSaveResponse(
                this.speackerService.create(this.speacker));
        }
    }

    private subscribeToSaveResponse(result: Observable<Speacker>) {
        result.subscribe((res: Speacker) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: Speacker) {
        this.eventManager.broadcast({ name: 'speackerListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }
}

@Component({
    selector: 'jhi-speacker-popup',
    template: ''
})
export class SpeackerPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private speackerPopupService: SpeackerPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.speackerPopupService
                    .open(SpeackerDialogComponent as Component, params['id']);
            } else {
                this.speackerPopupService
                    .open(SpeackerDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
