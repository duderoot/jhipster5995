import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Speacker } from './speacker.model';
import { SpeackerPopupService } from './speacker-popup.service';
import { SpeackerService } from './speacker.service';

@Component({
    selector: 'jhi-speacker-delete-dialog',
    templateUrl: './speacker-delete-dialog.component.html'
})
export class SpeackerDeleteDialogComponent {

    speacker: Speacker;

    constructor(
        private speackerService: SpeackerService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.speackerService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'speackerListModification',
                content: 'Deleted an speacker'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-speacker-delete-popup',
    template: ''
})
export class SpeackerDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private speackerPopupService: SpeackerPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.speackerPopupService
                .open(SpeackerDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
